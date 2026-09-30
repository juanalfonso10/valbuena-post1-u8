package com.example.auditoria.usecase.impl;

import com.example.auditoria.domain.entity.HallazgoAuditoria;
import com.example.auditoria.domain.valueobject.EstadoHallazgo;
import com.example.auditoria.domain.valueobject.HallazgoId;
import com.example.auditoria.usecase.CerrarHallazgoUseCase;
import com.example.auditoria.usecase.port.HallazgoRepositoryPort;
import com.example.auditoria.usecase.port.HistorialAuditoriaPort;

public class CerrarHallazgoConHistorial implements CerrarHallazgoUseCase {

    private final CerrarHallazgoUseCase delegado;
    private final HallazgoRepositoryPort repository;
    private final HistorialAuditoriaPort historial;

    public CerrarHallazgoConHistorial(CerrarHallazgoUseCase delegado,
                                      HallazgoRepositoryPort repository,
                                      HistorialAuditoriaPort historial) {
        this.delegado = delegado;
        this.repository = repository;
        this.historial = historial;
    }

    @Override
    public HallazgoAuditoria ejecutar(HallazgoId id) {
        EstadoHallazgo anterior = repository.buscarPorId(id).map(HallazgoAuditoria::getEstado).orElse(null);
        HallazgoAuditoria actualizado = delegado.ejecutar(id);
        historial.registrar(id, anterior, actualizado.getEstado(), "auditor");
        return actualizado;
    }
}
