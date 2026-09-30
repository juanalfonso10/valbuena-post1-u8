package com.example.auditoria.adapter.out.persistence;

import com.example.auditoria.domain.valueobject.EstadoHallazgo;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_cambios_estado")
public class HistorialCambioEstadoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long hallazgoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private EstadoHallazgo estadoOrigen;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoHallazgo estadoDestino;

    @Column(nullable = false)
    private String usuario;

    @Column(nullable = false)
    private LocalDateTime fechaCambio;

    public HistorialCambioEstadoEntity() {}

    public HistorialCambioEstadoEntity(Long hallazgoId, EstadoHallazgo estadoOrigen,
                                       EstadoHallazgo estadoDestino, String usuario, LocalDateTime fechaCambio) {
        this.hallazgoId = hallazgoId;
        this.estadoOrigen = estadoOrigen;
        this.estadoDestino = estadoDestino;
        this.usuario = usuario;
        this.fechaCambio = fechaCambio;
    }

    public Long getId() { return id; }
    public Long getHallazgoId() { return hallazgoId; }
    public EstadoHallazgo getEstadoOrigen() { return estadoOrigen; }
    public EstadoHallazgo getEstadoDestino() { return estadoDestino; }
    public String getUsuario() { return usuario; }
    public LocalDateTime getFechaCambio() { return fechaCambio; }
}
