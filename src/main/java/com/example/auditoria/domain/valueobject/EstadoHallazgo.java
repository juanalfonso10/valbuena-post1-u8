package com.example.auditoria.domain.valueobject;

public enum EstadoHallazgo {
    ABIERTO {
        @Override
        public boolean puedeTransicionarA(EstadoHallazgo destino) {
            return destino == EN_REMEDIACION;
        }
    },
    EN_REMEDIACION {
        @Override
        public boolean puedeTransicionarA(EstadoHallazgo destino) {
            return destino == CERRADO;
        }
    },
    CERRADO {
        @Override
        public boolean puedeTransicionarA(EstadoHallazgo destino) {
            return destino == REABIERTO;
        }
    },
    REABIERTO {
        @Override
        public boolean puedeTransicionarA(EstadoHallazgo destino) {
            return destino == EN_REMEDIACION;
        }
    };

    public abstract boolean puedeTransicionarA(EstadoHallazgo destino);
}
