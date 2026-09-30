# Post-contenido — Unidad 8: Patrones Arquitectónicos II

## Descripción
Repositorio del post-contenido de la Unidad 8 de Patrones de Diseño de Software. Sistema de seguimiento de hallazgos de auditoría interna implementado mediante Clean Architecture en la Parte 1, y extendido con un dashboard agregado y una bitácora de trazabilidad en la Parte 2.

## Parte 1 — Clean Architecture (Hallazgos de Auditoría)
El proyecto organiza sus componentes en los cuatro círculos concéntricos:
- **Entities (`domain/`)**: Contiene el Aggregate Root `HallazgoAuditoria`, los Value Objects (`HallazgoId`, `Severidad`, `PlanRemediacion`) y el enum con máquina de estados `EstadoHallazgo`. Ninguna clase de este círculo importa frameworks externos.
- **Use Cases (`usecase/`)**: Define las interfaces de los casos de uso, los puertos de salida (`HallazgoRepositoryPort`) y las implementaciones de la lógica de negocio pura.
- **Interface Adapters (`adapter/`)**: Contiene los adaptadores de entrada (`HallazgoController` y DTOs) y de salida (`HallazgoRepositoryAdapter` junto a la entidad JPA).
- **Frameworks & Drivers (`config/` y aplicación)**: Configuración explícita de beans mediante `AuditoriaConfiguration` y arranque de Spring Boot.

## Parte 2 — Análisis costo-beneficio de CQRS y Event Sourcing
Aplicando los criterios de selección de la guía de la unidad:
1. **Escala y carga:** Tratándose de un sistema académico/interno de baja concurrencia, no existe una disparidad masiva entre lecturas y escrituras que justifique segregar la infraestructura física de bases de datos.
2. **Complejidad de consultas:** Los requerimientos del dashboard (conteos y promedios) se resuelven de forma óptima y limpia mediante consultas JPQL con proyecciones (`GROUP BY`) sobre el mismo esquema relacional.
3. **Consistencia:** El comité de auditoría no requiere una vista en tiempo real ultra-reactiva; la consistencia inmediata estándar de una base de datos relacional es totalmente suficiente.
4. **Naturaleza de la trazabilidad:** El área de Cumplimiento exige reconstruir la secuencia cronológica de cambios de estado, mas no auditar mediante *Event Sourcing* completo (el cual implicaría reescribir la persistencia del agregado por *replay* de eventos). Una tabla de historial *append-only* coadyuva perfectamente sin introducir complejidad innecesaria.
5. **Señales de sobre-ingeniería:** Ante un equipo reducido y sin un dominio masivo de eventos complejos, implementar CQRS y Event Sourcing completos constituiría una sobre-ingeniería injustificada. Se optó por una extensión liviana y pragmática sobre el mismo repositorio y un modelo de bitácora independiente.

## Decisiones de diseño
1. **Severidad (enum simple) vs. EstadoHallazgo (enum con máquina de estados):** Se dotó de comportamiento a `EstadoHallazgo` porque encapsula reglas de transición de negocio estrictas, mientras que `Severidad` es meramente clasificatoria.
2. **PlanRemediacion como Value Object embebido:** Se mantuvo dentro del agregado `HallazgoAuditoria` para garantizar la invariante transaccional de que ningún hallazgo puede pasar a remediación o cerrarse sin un plan válido.
3. **Extensión del repositorio existente en vez de CQRS completo:** Se extendió `HallazgoRepositoryPort` con métodos de proyección (`count`, `avg`) aprovechando Projections de Spring Data JPA, evitando la duplicación de capas de lectura.
4. **Bitácora simple append-only vs. Event Store:** Se implementó `HistorialCambioEstado` como una tabla adicional de inserción exclusiva en la misma transacción, preservando el estado actual del agregado y satisfaciendo la trazabilidad legal sin alterar la fuente de verdad.

## Cómo ejecutar
    $mvn clean package$ mvn spring-boot:run

## Herramientas utilizadas
- Java 17, Spring Boot 3.x, Spring Data JPA, H2
- Apache Maven, Git, GitHub

## Conclusiones
Clean Architecture protege la lógica de negocio al aislarla por completo de los detalles de infraestructura mediante la regla estricta de dependencia hacia adentro. Asimismo, el análisis costo-beneficio demuestra que los patrones avanzados como CQRS y Event Sourcing deben reservarse estrictamente para escenarios de alta escala y complejidad de dominio, prefiriendo soluciones pragmáticas y sencillas cuando los requerimientos de negocio pueden resolverse con proyecciones y bitácoras directas.
