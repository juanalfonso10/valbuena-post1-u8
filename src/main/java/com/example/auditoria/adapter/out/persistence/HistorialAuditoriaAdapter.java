package com.example.auditoria.adapter.out.persistence;

import com.example.auditoria.domain.valueobject.EstadoHallazgo;
import com.example.auditoria.domain.valueobject.HallazgoId;
import com.example.auditoria.usecase.port.CambioEstadoView;
import com.example.auditoria.usecase.port.HistorialAuditoriaPort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class HistorialAuditoriaAdapter implements HistorialAuditoriaPort {

    private final HistorialCambioEstadoJpaRepository jpa;

    public HistorialAuditoriaAdapter(HistorialCambioEstadoJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void registrar(HallazgoId hallazgoId, EstadoHallazgo anterior, EstadoHallazgo nuevo, String usuario) {
        jpa.save(new HistorialCambioEstadoEntity(
                hallazgoId.getValor(), anterior, nuevo, usuario, LocalDateTime.now()));
    }

    @Override
    public List<CambioEstadoView> listarPorHallazgo(HallazgoId hallazgoId) {
        return jpa.findByHallazgoIdOrderByFechaCambioAsc(hallazgoId.getValor()).stream()
                .map(e -> new CambioEstadoView(
                        e.getEstadoOrigen() == null ? null : e.getEstadoOrigen().name(),
                        e.getEstadoDestino().name(),
                        e.getUsuario(),
                        e.getFechaCambio()))
                .toList();
    }
}
