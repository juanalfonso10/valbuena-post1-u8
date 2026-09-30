package com.example.auditoria.usecase;

import com.example.auditoria.domain.entity.HallazgoAuditoria;
import com.example.auditoria.domain.valueobject.Severidad;

public interface RegistrarHallazgoUseCase {
    HallazgoAuditoria ejecutar(String titulo, String descripcion, Severidad severidad, String areaResponsable);
}
