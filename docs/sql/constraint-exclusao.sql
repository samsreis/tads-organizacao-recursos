CREATE EXTENSION IF NOT EXISTS btree_gist;

ALTER TABLE reservas ADD CONSTRAINT reserva_sem_sobreposicao
EXCLUDE USING gist (
    sala_id WITH =,
    tstzrange(inicio, fim) WITH &&
)
WHERE (status NOT IN ('CANCELADA', 'REJEITADA'));
