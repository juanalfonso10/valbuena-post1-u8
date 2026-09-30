package com.example.auditoria.adapter.in.web;

import com.example.auditoria.adapter.in.web.dto.*;
import com.example.auditoria.adapter.out.persistence.HistorialCambioEstadoEntity;
import com.example.auditoria.adapter.out.persistence.HistorialCambioEstadoJpaRepository;
import com.example.auditoria.domain.entity.HallazgoAuditoria;
import com.example.auditoria.domain.valueobject.EstadoHallazgo;
import com.example.auditoria.domain.valueobject.HallazgoId;
import com.example.auditoria.domain.valueobject.PlanRemediacion;
import com.example.auditoria.domain.valueobject.TransicionInvalidaException;
import com.example.auditoria.usecase.*;
import com.example.auditoria.usecase.port.DashboardAuditoriaView;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hallazgos")
public class HallazgoController {

    private final RegistrarHallazgoUseCase registrarUseCase;
    private final IniciarRemediacionUseCase iniciarRemediacionUseCase;
    private final CerrarHallazgoUseCase cerrarUseCase;
    private final ReabrirHallazgoUseCase reabrirUseCase;
    private final ConsultarHallazgoUseCase consultarUseCase;
    private final ObtenerDashboardAuditoriaUseCase dashboardUseCase;
    private final HistorialCambioEstadoJpaRepository historialRepository;

    public HallazgoController(RegistrarHallazgoUseCase registrarUseCase,
                              IniciarRemediacionUseCase iniciarRemediacionUseCase,
                              CerrarHallazgoUseCase cerrarUseCase,
                              ReabrirHallazgoUseCase reabrirUseCase,
                              ConsultarHallazgoUseCase consultarUseCase,
                              ObtenerDashboardAuditoriaUseCase dashboardUseCase,
                              HistorialCambioEstadoJpaRepository historialRepository) {
        this.registrarUseCase = registrarUseCase;
        this.iniciarRemediacionUseCase = iniciarRemediacionUseCase;
        this.cerrarUseCase = cerrarUseCase;
        this.reabrirUseCase = reabrirUseCase;
        this.consultarUseCase = consultarUseCase;
        this.dashboardUseCase = dashboardUseCase;
        this.historialRepository = historialRepository;
    }

    @PostMapping
    public ResponseEntity<HallazgoResponse> registrar(@Valid @RequestBody RegistrarHallazgoRequest req) {
        HallazgoAuditoria creado = registrarUseCase.ejecutar(req.titulo(), req.descripcion(), req.severidad(), req.areaResponsable());
        historialRepository.save(new HistorialCambioEstadoEntity(
            creado.getId().getValor(), null, EstadoHallazgo.ABIERTO, "sistema", LocalDateTime.now()));
        return ResponseEntity.status(HttpStatus.CREATED).body(HallazgoResponse.fromDomain(creado));
    }

    @GetMapping
    public List<HallazgoResponse> listar() {
        return consultarUseCase.listarTodos().stream().map(HallazgoResponse::fromDomain).toList();
    }

    @GetMapping("/{id}")
    public HallazgoResponse buscarPorId(@PathVariable Long id) {
        return HallazgoResponse.fromDomain(consultarUseCase.buscarPorId(new HallazgoId(id)));
    }

    @PatchMapping("/{id}/iniciar-remediacion")
    public HallazgoResponse iniciarRemediacion(@PathVariable Long id, @Valid @RequestBody IniciarRemediacionRequest req) {
        EstadoHallazgo previo = consultarUseCase.buscarPorId(new HallazgoId(id)).getEstado();
        PlanRemediacion plan = new PlanRemediacion(req.descripcion(), req.responsable(), req.fechaCompromiso());
        HallazgoAuditoria actualizado = iniciarRemediacionUseCase.ejecutar(new HallazgoId(id), plan);
        historialRepository.save(new HistorialCambioEstadoEntity(
            id, previo, EstadoHallazgo.EN_REMEDIACION, req.responsable(), LocalDateTime.now()));
        return HallazgoResponse.fromDomain(actualizado);
    }

    @PatchMapping("/{id}/cerrar")
    public HallazgoResponse cerrar(@PathVariable Long id) {
        EstadoHallazgo previo = consultarUseCase.buscarPorId(new HallazgoId(id)).getEstado();
        HallazgoAuditoria actualizado = cerrarUseCase.ejecutar(new HallazgoId(id));
        historialRepository.save(new HistorialCambioEstadoEntity(
            id, previo, EstadoHallazgo.CERRADO, "auditor", LocalDateTime.now()));
        return HallazgoResponse.fromDomain(actualizado);
    }

    @PatchMapping("/{id}/reabrir")
    public HallazgoResponse reabrir(@PathVariable Long id, @Valid @RequestBody ReabrirRequest req) {
        EstadoHallazgo previo = consultarUseCase.buscarPorId(new HallazgoId(id)).getEstado();
        HallazgoAuditoria actualizado = reabrirUseCase.ejecutar(new HallazgoId(id), req.motivo());
        historialRepository.save(new HistorialCambioEstadoEntity(
            id, previo, EstadoHallazgo.REABIERTO, "auditor-lider", LocalDateTime.now()));
        return HallazgoResponse.fromDomain(actualizado);
    }

    @GetMapping("/dashboard")
    public DashboardAuditoriaView obtenerDashboard() {
        return dashboardUseCase.ejecutar();
    }

    @GetMapping("/{id}/historial")
    public List<HistorialCambioEstadoEntity> obtenerHistorial(@PathVariable Long id) {
        return historialRepository.findByHallazgoIdOrderByFechaCambioAsc(id);
    }

    @ExceptionHandler(TransicionInvalidaException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleTransicion(TransicionInvalidaException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleArgument(IllegalArgumentException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e -> errores.put(e.getField(), e.getDefaultMessage()));
        return errores;
    }
}
