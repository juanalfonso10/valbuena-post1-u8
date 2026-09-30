package com.example.auditoria.adapter.out.persistence;

import com.example.auditoria.usecase.port.ConteoCategoria;
import com.example.auditoria.usecase.port.PromedioCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HallazgoJpaRepository extends JpaRepository<HallazgoJpaEntity, Long> {

    @Query("SELECT h.severidad as categoria, COUNT(h) as cantidad FROM HallazgoJpaEntity h GROUP BY h.severidad")
    List<ConteoCategoria> contarPorSeveridad();

    @Query("SELECT h.estado as categoria, COUNT(h) as cantidad FROM HallazgoJpaEntity h GROUP BY h.estado")
    List<ConteoCategoria> contarPorEstado();

    @Query("SELECT h.areaResponsable as area, AVG(DATEDIFF(day, h.fechaDeteccion, h.fechaCierre)) as promedioDias " +
           "FROM HallazgoJpaEntity h WHERE h.fechaCierre IS NOT NULL GROUP BY h.areaResponsable")
    List<PromedioCategoria> calcularPromedioDiasPorArea();
}
