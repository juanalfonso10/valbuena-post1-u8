package com.example.auditoria.adapter.in.web.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record IniciarRemediacionRequest(
    @NotBlank(message = "La descripcion del plan es obligatoria") String descripcion,
    @NotBlank(message = "El responsable es obligatorio") String responsable,
    @NotNull(message = "La fecha es obligatoria") @Future(message = "La fecha de compromiso debe ser futura") LocalDate fechaCompromiso
) {}
