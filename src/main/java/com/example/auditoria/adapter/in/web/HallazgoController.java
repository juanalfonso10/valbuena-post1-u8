package com.example.auditoria.adapter.in.web;

import com.example.auditoria.adapter.in.web.dto.*;
import com.example.auditoria.domain.entity.HallazgoAuditoria;
import com.example.auditoria.domain.valueobject.HallazgoId;
import com.example.auditoria.domain.valueobject.PlanRemediacion;
import com.example.auditoria.domain.valueobject.TransicionInvalidaException;
import com.example.auditoria.usecase.*;
import com.example.auditoria.usecase.port.CambioEstadoView;
import com.example.auditoria.usecase.port.DashboardAuditoriaView;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

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
    private final ConsultarHistorialUseCase consultarHistorialUseCase;

    public HallazgoController(RegistrarHallazgoUseCase registrarUseCase,
                              IniciarRemediacionUseCase iniciarRemediacionUseCase,
                              CerrarHallazgoUseCase cerrarUseCase,
                              ReabrirHallazgoUseCase reabrirUseCase,
                              ConsultarHallazgoUseCase consultarUseCase,
                              ObtenerDashboardAuditoriaUseCase dashboardUseCase,
                              ConsultarHistorialUseCase consultarHistorialUseCase) {
        this.registrarUseCase = registrarUseCase;
        this.iniciarRemediacionUseCase = iniciarRemediacionUseCase;
        this.cerrarUseCase = cerrarUseCase;
        this.reabrirUseCase = reabrirUseCase;
        this.consultarUseCase = consultarUseCase;
        this.dashboardUseCase = dashboardUseCase;
        this.consultarHistorialUseCase = consultarHistorialUseCase;
    }

    @PostMapping
    public ResponseEntity<HallazgoResponse> registrar(@Valid @RequestBody RegistrarHallazgoRequest req) {
        HallazgoAuditoria creado = registrarUseCase.ejecutar(
                req.titulo(), req.descripcion(), req.severidad(), req.areaResponsable());
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
    public HallazgoResponse iniciarRemediacion(@PathVariable Long id,
                                               @Valid @RequestBody IniciarRemediacionRequest req) {
        PlanRemediacion plan = new PlanRemediacion(req.descripcion(), req.responsable(), req.fechaCompromiso());
        return HallazgoResponse.fromDomain(iniciarRemediacionUseCase.ejecutar(new HallazgoId(id), plan));
    }

    @PatchMapping("/{id}/cerrar")
    public HallazgoResponse cerrar(@PathVariable Long id) {
        return HallazgoResponse.fromDomain(cerrarUseCase.ejecutar(new HallazgoId(id)));
    }

    @PatchMapping("/{id}/reabrir")
    public HallazgoResponse reabrir(@PathVariable Long id, @Valid @RequestBody ReabrirRequest req) {
        return HallazgoResponse.fromDomain(reabrirUseCase.ejecutar(new HallazgoId(id), req.motivo()));
    }

    @GetMapping("/dashboard")
    public DashboardAuditoriaView obtenerDashboard() {
        return dashboardUseCase.ejecutar();
    }

    @GetMapping("/{id}/historial")
    public List<CambioEstadoView> obtenerHistorial(@PathVariable Long id) {
        return consultarHistorialUseCase.ejecutar(new HallazgoId(id));
    }

    @ExceptionHandler({TransicionInvalidaException.class, IllegalStateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleTransicion(RuntimeException ex) {
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
