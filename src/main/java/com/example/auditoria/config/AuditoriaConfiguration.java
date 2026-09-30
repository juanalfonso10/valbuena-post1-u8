package com.example.auditoria.config;

import com.example.auditoria.adapter.out.persistence.HallazgoJpaRepository;
import com.example.auditoria.usecase.*;
import com.example.auditoria.usecase.impl.*;
import com.example.auditoria.usecase.port.HallazgoRepositoryPort;
import com.example.auditoria.usecase.port.HistorialAuditoriaPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuditoriaConfiguration {

    @Bean
    public RegistrarHallazgoUseCase registrarHallazgoUseCase(HallazgoRepositoryPort repo,
                                                             HistorialAuditoriaPort historial) {
        return new RegistrarHallazgoConHistorial(new RegistrarHallazgoService(repo), historial);
    }

    @Bean
    public IniciarRemediacionUseCase iniciarRemediacionUseCase(HallazgoRepositoryPort repo,
                                                               HistorialAuditoriaPort historial) {
        return new IniciarRemediacionConHistorial(new IniciarRemediacionService(repo), repo, historial);
    }

    @Bean
    public CerrarHallazgoUseCase cerrarHallazgoUseCase(HallazgoRepositoryPort repo,
                                                       HistorialAuditoriaPort historial) {
        return new CerrarHallazgoConHistorial(new CerrarHallazgoService(repo), repo, historial);
    }

    @Bean
    public ReabrirHallazgoUseCase reabrirHallazgoUseCase(HallazgoRepositoryPort repo,
                                                         HistorialAuditoriaPort historial) {
        return new ReabrirHallazgoConHistorial(new ReabrirHallazgoService(repo), repo, historial);
    }

    @Bean
    public ConsultarHallazgoUseCase consultarHallazgoUseCase(HallazgoRepositoryPort repo) {
        return new ConsultarHallazgoService(repo);
    }

    @Bean
    public ConsultarHistorialUseCase consultarHistorialUseCase(HistorialAuditoriaPort historial) {
        return new ConsultarHistorialService(historial);
    }

    @Bean
    public ObtenerDashboardAuditoriaUseCase obtenerDashboardAuditoriaUseCase(HallazgoJpaRepository jpaRepository) {
        return new ObtenerDashboardAuditoriaService(jpaRepository);
    }
}
