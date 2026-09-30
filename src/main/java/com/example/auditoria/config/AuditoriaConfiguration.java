package com.example.auditoria.config;

import com.example.auditoria.usecase.*;
import com.example.auditoria.usecase.impl.*;
import com.example.auditoria.usecase.port.HallazgoRepositoryPort;
import com.example.auditoria.usecase.port.HistorialAuditoriaPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class AuditoriaConfiguration {

    @Bean
    public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }

    @Bean
    public RegistrarHallazgoUseCase registrarHallazgoUseCase(HallazgoRepositoryPort repo,
                                                             HistorialAuditoriaPort historial,
                                                             TransactionTemplate tx) {
        RegistrarHallazgoUseCase useCase =
                new RegistrarHallazgoConHistorial(new RegistrarHallazgoService(repo), historial);
        return (titulo, descripcion, severidad, area) ->
                tx.execute(status -> useCase.ejecutar(titulo, descripcion, severidad, area));
    }

    @Bean
    public IniciarRemediacionUseCase iniciarRemediacionUseCase(HallazgoRepositoryPort repo,
                                                               HistorialAuditoriaPort historial,
                                                               TransactionTemplate tx) {
        IniciarRemediacionUseCase useCase =
                new IniciarRemediacionConHistorial(new IniciarRemediacionService(repo), repo, historial);
        return (id, plan) -> tx.execute(status -> useCase.ejecutar(id, plan));
    }

    @Bean
    public CerrarHallazgoUseCase cerrarHallazgoUseCase(HallazgoRepositoryPort repo,
                                                       HistorialAuditoriaPort historial,
                                                       TransactionTemplate tx) {
        CerrarHallazgoUseCase useCase =
                new CerrarHallazgoConHistorial(new CerrarHallazgoService(repo), repo, historial);
        return id -> tx.execute(status -> useCase.ejecutar(id));
    }

    @Bean
    public ReabrirHallazgoUseCase reabrirHallazgoUseCase(HallazgoRepositoryPort repo,
                                                         HistorialAuditoriaPort historial,
                                                         TransactionTemplate tx) {
        ReabrirHallazgoUseCase useCase =
                new ReabrirHallazgoConHistorial(new ReabrirHallazgoService(repo), repo, historial);
        return (id, motivo) -> tx.execute(status -> useCase.ejecutar(id, motivo));
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
    public ObtenerDashboardAuditoriaUseCase obtenerDashboardAuditoriaUseCase(HallazgoRepositoryPort repo) {
        return new ObtenerDashboardAuditoriaService(repo);
    }
}
