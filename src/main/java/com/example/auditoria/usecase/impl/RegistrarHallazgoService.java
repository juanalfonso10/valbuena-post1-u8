package com.example.auditoria.usecase.impl;

import com.example.auditoria.domain.entity.HallazgoAuditoria;
import com.example.auditoria.domain.valueobject.Severidad;
import com.example.auditoria.usecase.RegistrarHallazgoUseCase;
import com.example.auditoria.usecase.port.HallazgoRepositoryPort;

public class RegistrarHallazgoService implements RegistrarHallazgoUseCase {
    private final HallazgoRepositoryPort repository;

    public RegistrarHallazgoService(HallazgoRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public HallazgoAuditoria ejecutar(String titulo, String descripcion, Severidad severidad, String areaResponsable) {
        HallazgoAuditoria nuevo = new HallazgoAuditoria(null, titulo, descripcion, severidad, areaResponsable);
        return repository.guardar(nuevo);
    }
}
