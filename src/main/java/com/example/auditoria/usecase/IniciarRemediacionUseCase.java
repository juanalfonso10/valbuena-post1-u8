package com.example.auditoria.usecase;

import com.example.auditoria.domain.entity.HallazgoAuditoria;
import com.example.auditoria.domain.valueobject.HallazgoId;
import com.example.auditoria.domain.valueobject.PlanRemediacion;

public interface IniciarRemediacionUseCase {
    HallazgoAuditoria ejecutar(HallazgoId id, PlanRemediacion plan);
}
