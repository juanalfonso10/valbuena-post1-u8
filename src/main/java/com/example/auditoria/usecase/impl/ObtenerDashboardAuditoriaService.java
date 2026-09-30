package com.example.auditoria.usecase.impl;

import com.example.auditoria.usecase.ObtenerDashboardAuditoriaUseCase;
import com.example.auditoria.usecase.port.DashboardAuditoriaView;
import com.example.auditoria.usecase.port.HallazgoRepositoryPort;

public class ObtenerDashboardAuditoriaService implements ObtenerDashboardAuditoriaUseCase {
    private final HallazgoRepositoryPort repository;

    public ObtenerDashboardAuditoriaService(HallazgoRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public DashboardAuditoriaView ejecutar() {
        return new DashboardAuditoriaView(
            repository.contarPorSeveridad(),
            repository.contarPorEstado(),
            repository.calcularPromedioDiasPorArea()
        );
    }
}
