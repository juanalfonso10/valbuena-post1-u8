package com.example.auditoria;

import com.example.auditoria.adapter.in.web.HallazgoController;
import com.example.auditoria.adapter.in.web.dto.IniciarRemediacionRequest;
import com.example.auditoria.adapter.in.web.dto.RegistrarHallazgoRequest;
import com.example.auditoria.domain.valueobject.EstadoHallazgo;
import com.example.auditoria.domain.valueobject.Severidad;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuditoriaApplicationTests {

    @Autowired
    private HallazgoController controller;

    @Test
    void testCicloCompletoHallazgoYHistorial() {
        var res = controller.registrar(new RegistrarHallazgoRequest(
                "Fuga de datos", "Puerto abierto", Severidad.CRITICA, "Seguridad"));
        Long id = res.getBody().id();
        assertNotNull(id);

        var rem = controller.iniciarRemediacion(id, new IniciarRemediacionRequest(
                "Cerrar firewall", "Admin Red", LocalDate.now().plusDays(5)));
        assertEquals(EstadoHallazgo.EN_REMEDIACION, rem.estado());

        var cerrado = controller.cerrar(id);
        assertEquals(EstadoHallazgo.CERRADO, cerrado.estado());

        var historial = controller.obtenerHistorial(id);
        assertTrue(historial.size() >= 3);

        var dashboard = controller.obtenerDashboard();
        assertNotNull(dashboard);
    }
}
