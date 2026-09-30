package com.example.auditoria.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialCambioEstadoJpaRepository extends JpaRepository<HistorialCambioEstadoEntity, Long> {
    List<HistorialCambioEstadoEntity> findByHallazgoIdOrderByFechaCambioAsc(Long hallazgoId);
}
