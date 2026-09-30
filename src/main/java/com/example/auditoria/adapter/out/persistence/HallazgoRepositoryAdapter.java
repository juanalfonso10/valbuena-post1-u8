package com.example.auditoria.adapter.out.persistence;

import com.example.auditoria.domain.entity.HallazgoAuditoria;
import com.example.auditoria.domain.valueobject.HallazgoId;
import com.example.auditoria.domain.valueobject.PlanRemediacion;
import com.example.auditoria.usecase.port.HallazgoRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class HallazgoRepositoryAdapter implements HallazgoRepositoryPort {
    private final HallazgoJpaRepository jpaRepository;

    public HallazgoRepositoryAdapter(HallazgoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public HallazgoAuditoria guardar(HallazgoAuditoria hallazgo) {
        HallazgoJpaEntity entity = toEntity(hallazgo);
        HallazgoJpaEntity guardado = jpaRepository.save(entity);
        return toDomain(guardado);
    }

    @Override
    public Optional<HallazgoAuditoria> buscarPorId(HallazgoId id) {
        return jpaRepository.findById(id.getValor()).map(this::toDomain);
    }

    @Override
    public List<HallazgoAuditoria> listarTodos() {
        return jpaRepository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    private HallazgoJpaEntity toEntity(HallazgoAuditoria domain) {
        HallazgoJpaEntity e = new HallazgoJpaEntity();
        if (domain.getId() != null) {
            e.setId(domain.getId().getValor());
        }
        e.setTitulo(domain.getTitulo());
        e.setDescripcion(domain.getDescripcion());
        e.setSeveridad(domain.getSeveridad());
        e.setEstado(domain.getEstado());
        e.setAreaResponsable(domain.getAreaResponsable());
        e.setFechaDeteccion(domain.getFechaDeteccion());
        e.setFechaCierre(domain.getFechaCierre());
        if (domain.getPlanRemediacion() != null) {
            e.setPlanDescripcion(domain.getPlanRemediacion().getDescripcion());
            e.setPlanResponsable(domain.getPlanRemediacion().getResponsable());
            e.setPlanFechaCompromiso(domain.getPlanRemediacion().getFechaCompromiso());
        }
        return e;
    }

    private HallazgoAuditoria toDomain(HallazgoJpaEntity e) {
        PlanRemediacion plan = null;
        if (e.getPlanDescripcion() != null) {
            plan = new PlanRemediacion(e.getPlanDescripcion(), e.getPlanResponsable(), e.getPlanFechaCompromiso());
        }
        return new HallazgoAuditoria(
                new HallazgoId(e.getId()),
                e.getTitulo(),
                e.getDescripcion(),
                e.getSeveridad(),
                e.getEstado(),
                e.getAreaResponsable(),
                e.getFechaDeteccion(),
                e.getFechaCierre(),
                plan
        );
    }
}
