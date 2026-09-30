package com.example.auditoria.adapter.in.web.dto;

import com.example.auditoria.domain.valueobject.Severidad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistrarHallazgoRequest(
    @NotBlank(message = "El titulo es obligatorio") String titulo,
    @NotBlank(message = "La descripcion es obligatoria") String descripcion,
    @NotNull(message = "La severidad es obligatoria") Severidad severidad,
    @NotBlank(message = "El area responsable es obligatoria") String areaResponsable
) {}
