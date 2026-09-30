package com.example.auditoria.usecase.impl;

import com.example.auditoria.domain.entity.HallazgoAuditoria;
import com.example.auditoria.domain.valueobject.Severidad;
import com.example.auditoria.usecase.RegistrarHallazgoUseCase;
import com.example.auditoria.usecase.port.HistorialAuditoriaPort;

public class RegistrarHallazgoConHistorial implements RegistrarHallazgoUseCase {

    private final RegistrarHallazgoUseCase delegado;
    private final HistorialAuditoriaPort historial;

    public RegistrarHallazgoConHistorial(RegistrarHallazgoUseCase delegado, HistorialAuditoriaPort historial) {
        this.delegado = delegado;
        this.historial = historial;
    }

    @Override
    public HallazgoAuditoria ejecutar(String titulo, String descripcion, Severidad severidad, String areaResponsable) {
        HallazgoAuditoria creado = delegado.ejecutar(titulo, descripcion, severidad, areaResponsable);
        historial.registrar(creado.getId(), null, creado.getEstado(), "sistema");
        return creado;
    }
}
