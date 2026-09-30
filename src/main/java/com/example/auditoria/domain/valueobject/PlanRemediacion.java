package com.example.auditoria.domain.valueobject;

import java.time.LocalDate;
import java.util.Objects;

public final class PlanRemediacion {
    private final String descripcion;
    private final String responsable;
    private final LocalDate fechaCompromiso;

    public PlanRemediacion(String descripcion, String responsable, LocalDate fechaCompromiso) {
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripcion del plan es obligatoria");
        }
        if (responsable == null || responsable.isBlank()) {
            throw new IllegalArgumentException("El responsable es obligatorio");
        }
        if (fechaCompromiso == null || fechaCompromiso.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de compromiso debe ser futura");
        }
        this.descripcion = descripcion;
        this.responsable = responsable;
        this.fechaCompromiso = fechaCompromiso;
    }

    public String getDescripcion() { return descripcion; }
    public String getResponsable() { return responsable; }
    public LocalDate getFechaCompromiso() { return fechaCompromiso; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanRemediacion that = (PlanRemediacion) o;
        return Objects.equals(descripcion, that.descripcion) &&
               Objects.equals(responsable, that.responsable) &&
               Objects.equals(fechaCompromiso, that.fechaCompromiso);
    }

    @Override
    public int hashCode() {
        return Objects.hash(descripcion, responsable, fechaCompromiso);
    }
}
