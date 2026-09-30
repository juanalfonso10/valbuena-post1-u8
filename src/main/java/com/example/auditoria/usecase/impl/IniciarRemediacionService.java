package com.example.auditoria.usecase.impl;

import com.example.auditoria.domain.entity.HallazgoAuditoria;
import com.example.auditoria.domain.valueobject.HallazgoId;
import com.example.auditoria.domain.valueobject.PlanRemediacion;
import com.example.auditoria.usecase.IniciarRemediacionUseCase;
import com.example.auditoria.usecase.port.HallazgoRepositoryPort;

public class IniciarRemediacionService implements IniciarRemediacionUseCase {
    private final HallazgoRepositoryPort repository;

    public IniciarRemediacionService(HallazgoRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public HallazgoAuditoria ejecutar(HallazgoId id, PlanRemediacion plan) {
        HallazgoAuditoria h = repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Hallazgo no encontrado: " + id));
        h.iniciarRemediacion(plan);
        return repository.guardar(h);
    }
}
