package com.example.auditoria.adapter.out.persistence;

import com.example.auditoria.domain.valueobject.EstadoHallazgo;
import com.example.auditoria.domain.valueobject.Severidad;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "hallazgos")
public class HallazgoJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(length = 1000)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severidad severidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoHallazgo estado;

    @Column(nullable = false)
    private String areaResponsable;

    private LocalDate fechaDeteccion;
    private LocalDate fechaCierre;

    private String planDescripcion;
    private String planResponsable;
    private LocalDate planFechaCompromiso;

    public HallazgoJpaEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Severidad getSeveridad() { return severidad; }
    public void setSeveridad(Severidad severidad) { this.severidad = severidad; }
    public EstadoHallazgo getEstado() { return estado; }
    public void setEstado(EstadoHallazgo estado) { this.estado = estado; }
    public String getAreaResponsable() { return areaResponsable; }
    public void setAreaResponsable(String areaResponsable) { this.areaResponsable = areaResponsable; }
    public LocalDate getFechaDeteccion() { return fechaDeteccion; }
    public void setFechaDeteccion(LocalDate fechaDeteccion) { this.fechaDeteccion = fechaDeteccion; }
    public LocalDate getFechaCierre() { return fechaCierre; }
    public void setFechaCierre(LocalDate fechaCierre) { this.fechaCierre = fechaCierre; }
    public String getPlanDescripcion() { return planDescripcion; }
    public void setPlanDescripcion(String planDescripcion) { this.planDescripcion = planDescripcion; }
    public String getPlanResponsable() { return planResponsable; }
    public void setPlanResponsable(String planResponsable) { this.planResponsable = planResponsable; }
    public LocalDate getPlanFechaCompromiso() { return planFechaCompromiso; }
    public void setPlanFechaCompromiso(LocalDate planFechaCompromiso) { this.planFechaCompromiso = planFechaCompromiso; }
}
