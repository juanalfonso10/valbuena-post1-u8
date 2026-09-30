package com.example.auditoria.usecase.impl;

import com.example.auditoria.adapter.out.persistence.HallazgoJpaRepository;
import com.example.auditoria.usecase.ObtenerDashboardAuditoriaUseCase;
import com.example.auditoria.usecase.port.DashboardAuditoriaView;

public class ObtenerDashboardAuditoriaService implements ObtenerDashboardAuditoriaUseCase {
    private final HallazgoJpaRepository jpaRepository;

    public ObtenerDashboardAuditoriaService(HallazgoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public DashboardAuditoriaView ejecutar() {
        return new DashboardAuditoriaView(
            jpaRepository.contarPorSeveridad(),
            jpaRepository.contarPorEstado(),
            jpaRepository.calcularPromedioDiasPorArea()
        );
    }
}
