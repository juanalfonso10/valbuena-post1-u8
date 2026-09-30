package com.example.auditoria.usecase.impl;

import com.example.auditoria.domain.entity.HallazgoAuditoria;
import com.example.auditoria.domain.valueobject.HallazgoId;
import com.example.auditoria.usecase.ConsultarHallazgoUseCase;
import com.example.auditoria.usecase.port.HallazgoRepositoryPort;

import java.util.List;

public class ConsultarHallazgoService implements ConsultarHallazgoUseCase {
    private final HallazgoRepositoryPort repository;

    public ConsultarHallazgoService(HallazgoRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public HallazgoAuditoria buscarPorId(HallazgoId id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Hallazgo no encontrado: " + id));
    }

    @Override
    public List<HallazgoAuditoria> listarTodos() {
        return repository.listarTodos();
    }
}
