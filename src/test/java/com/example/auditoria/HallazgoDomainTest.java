package com.example.auditoria;

import com.example.auditoria.domain.entity.HallazgoAuditoria;
import com.example.auditoria.domain.valueobject.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class HallazgoDomainTest {

    @Test
    void testMaquinaEstadosValida() {
        HallazgoAuditoria h = new HallazgoAuditoria(new HallazgoId(1L), "Deficiencia Control", "Desc", Severidad.ALTA, "Sistemas");
        assertEquals(EstadoHallazgo.ABIERTO, h.getEstado());

        PlanRemediacion plan = new PlanRemediacion("Aplicar parches", "Ingeniero", LocalDate.now().plusDays(10));
        h.iniciarRemediacion(plan);
        assertEquals(EstadoHallazgo.EN_REMEDIACION, h.getEstado());

        h.cerrar();
        assertEquals(EstadoHallazgo.CERRADO, h.getEstado());

        h.reabrir("Reincidencia");
        assertEquals(EstadoHallazgo.REABIERTO, h.getEstado());
    }

    @Test
    void testTransicionInvalidaLanzaExcepcion() {
        HallazgoAuditoria h = new HallazgoAuditoria(new HallazgoId(1L), "Deficiencia", "Desc", Severidad.BAJA, "Finanzas");
        assertThrows(TransicionInvalidaException.class, h::cerrar);
    }
}
