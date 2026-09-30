package com.example.auditoria.usecase.impl;

import com.example.auditoria.domain.entity.HallazgoAuditoria;
import com.example.auditoria.domain.valueobject.HallazgoId;
import com.example.auditoria.usecase.ReabrirHallazgoUseCase;
import com.example.auditoria.usecase.port.HallazgoRepositoryPort;

public class ReabrirHallazgoService implements ReabrirHallazgoUseCase {
    private final HallazgoRepositoryPort repository;

    public ReabrirHallazgoService(HallazgoRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public HallazgoAuditoria ejecutar(HallazgoId id, String motivo) {
        HallazgoAuditoria h = repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Hallazgo no encontrado: " + id));
        h.reabrir(motivo);
        return repository.guardar(h);
    }
}
