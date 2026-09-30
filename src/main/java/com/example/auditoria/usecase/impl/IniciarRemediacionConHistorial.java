package com.example.auditoria.usecase.impl;

import com.example.auditoria.domain.entity.HallazgoAuditoria;
import com.example.auditoria.domain.valueobject.EstadoHallazgo;
import com.example.auditoria.domain.valueobject.HallazgoId;
import com.example.auditoria.domain.valueobject.PlanRemediacion;
import com.example.auditoria.usecase.IniciarRemediacionUseCase;
import com.example.auditoria.usecase.port.HallazgoRepositoryPort;
import com.example.auditoria.usecase.port.HistorialAuditoriaPort;

public class IniciarRemediacionConHistorial implements IniciarRemediacionUseCase {

    private final IniciarRemediacionUseCase delegado;
    private final HallazgoRepositoryPort repository;
    private final HistorialAuditoriaPort historial;

    public IniciarRemediacionConHistorial(IniciarRemediacionUseCase delegado,
                                          HallazgoRepositoryPort repository,
                                          HistorialAuditoriaPort historial) {
        this.delegado = delegado;
        this.repository = repository;
        this.historial = historial;
    }

    @Override
    public HallazgoAuditoria ejecutar(HallazgoId id, PlanRemediacion plan) {
        EstadoHallazgo anterior = repository.buscarPorId(id).map(HallazgoAuditoria::getEstado).orElse(null);
        HallazgoAuditoria actualizado = delegado.ejecutar(id, plan);
        historial.registrar(id, anterior, actualizado.getEstado(), "auditor");
        return actualizado;
    }
}
