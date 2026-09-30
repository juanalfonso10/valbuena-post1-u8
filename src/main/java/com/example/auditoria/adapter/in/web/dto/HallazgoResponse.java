package com.example.auditoria.adapter.in.web.dto;

import com.example.auditoria.domain.entity.HallazgoAuditoria;
import com.example.auditoria.domain.valueobject.EstadoHallazgo;
import com.example.auditoria.domain.valueobject.Severidad;

import java.time.LocalDate;

public record HallazgoResponse(
    Long id,
    String titulo,
    String descripcion,
    Severidad severidad,
    EstadoHallazgo estado,
    String areaResponsable,
    LocalDate fechaDeteccion,
    LocalDate fechaCierre,
    String planDescripcion,
    String planResponsable
) {
    public static HallazgoResponse fromDomain(HallazgoAuditoria h) {
        return new HallazgoResponse(
            h.getId() != null ? h.getId().getValor() : null,
            h.getTitulo(),
            h.getDescripcion(),
            h.getSeveridad(),
            h.getEstado(),
            h.getAreaResponsable(),
            h.getFechaDeteccion(),
            h.getFechaCierre(),
            h.getPlanRemediacion() != null ? h.getPlanRemediacion().getDescripcion() : null,
            h.getPlanRemediacion() != null ? h.getPlanRemediacion().getResponsable() : null
        );
    }
}
