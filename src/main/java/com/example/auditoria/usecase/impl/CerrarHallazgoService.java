package com.example.auditoria.usecase.impl;

import com.example.auditoria.domain.entity.HallazgoAuditoria;
import com.example.auditoria.domain.valueobject.HallazgoId;
import com.example.auditoria.usecase.CerrarHallazgoUseCase;
import com.example.auditoria.usecase.port.HallazgoRepositoryPort;

public class CerrarHallazgoService implements CerrarHallazgoUseCase {
    private final HallazgoRepositoryPort repository;

    public CerrarHallazgoService(HallazgoRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public HallazgoAuditoria ejecutar(HallazgoId id) {
        HallazgoAuditoria h = repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Hallazgo no encontrado: " + id));
        h.cerrar();
        return repository.guardar(h);
    }
}
