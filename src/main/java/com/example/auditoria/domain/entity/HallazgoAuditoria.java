package com.example.auditoria.domain.entity;

import com.example.auditoria.domain.valueobject.*;

import java.time.LocalDate;

public class HallazgoAuditoria {
    private HallazgoId id;
    private String titulo;
    private String descripcion;
    private Severidad severidad;
    private EstadoHallazgo estado;
    private String areaResponsable;
    private LocalDate fechaDeteccion;
    private LocalDate fechaCierre;
    private PlanRemediacion planRemediacion;

    public HallazgoAuditoria(HallazgoId id, String titulo, String descripcion, Severidad severidad, String areaResponsable) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.severidad = severidad;
        this.areaResponsable = areaResponsable;
        this.estado = EstadoHallazgo.ABIERTO;
        this.fechaDeteccion = LocalDate.now();
    }

    public HallazgoAuditoria(HallazgoId id, String titulo, String descripcion, Severidad severidad,
                             EstadoHallazgo estado, String areaResponsable, LocalDate fechaDeteccion,
                             LocalDate fechaCierre, PlanRemediacion planRemediacion) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.severidad = severidad;
        this.estado = estado;
        this.areaResponsable = areaResponsable;
        this.fechaDeteccion = fechaDeteccion;
        this.fechaCierre = fechaCierre;
        this.planRemediacion = planRemediacion;
    }

    public void iniciarRemediacion(PlanRemediacion plan) {
        if (!this.estado.puedeTransicionarA(EstadoHallazgo.EN_REMEDIACION)) {
            throw new TransicionInvalidaException("No se puede iniciar remediacion desde el estado " + this.estado);
        }
        if (plan == null) {
            throw new IllegalArgumentException("El plan de remediacion no puede ser nulo");
        }
        this.planRemediacion = plan;
        this.estado = EstadoHallazgo.EN_REMEDIACION;
    }

    public void cerrar() {
        if (!this.estado.puedeTransicionarA(EstadoHallazgo.CERRADO)) {
            throw new TransicionInvalidaException("No se puede cerrar un hallazgo en estado " + this.estado);
        }
        this.estado = EstadoHallazgo.CERRADO;
        this.fechaCierre = LocalDate.now();
    }

    public void reabrir(String motivo) {
        if (!this.estado.puedeTransicionarA(EstadoHallazgo.REABIERTO)) {
            throw new TransicionInvalidaException("Solo se pueden reabrir hallazgos que esten CERRADOS. Estado actual: " + this.estado);
        }
        this.estado = EstadoHallazgo.REABIERTO;
        this.fechaCierre = null;
    }

    public HallazgoId getId() { return id; }
    public void setId(HallazgoId id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public Severidad getSeveridad() { return severidad; }
    public EstadoHallazgo getEstado() { return estado; }
    public String getAreaResponsable() { return areaResponsable; }
    public LocalDate getFechaDeteccion() { return fechaDeteccion; }
    public LocalDate getFechaCierre() { return fechaCierre; }
    public PlanRemediacion getPlanRemediacion() { return planRemediacion; }
}
