package br.edu.senac.recursos.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class InicializadorBanco {

    private static final Logger log = LoggerFactory.getLogger(InicializadorBanco.class);

    @Bean
    CommandLineRunner aplicarConstraintReservas(JdbcTemplate jdbc) {
        return args -> {
            jdbc.execute("CREATE EXTENSION IF NOT EXISTS btree_gist");
            jdbc.execute("""
                DO $$
                BEGIN
                    IF NOT EXISTS (
                        SELECT 1 FROM pg_constraint WHERE conname = 'reserva_sem_sobreposicao'
                    ) THEN
                        ALTER TABLE reservas ADD CONSTRAINT reserva_sem_sobreposicao
                        EXCLUDE USING gist (
                            sala_id WITH =,
                            tstzrange(inicio, fim) WITH &&
                        )
                        WHERE (status NOT IN ('CANCELADA', 'REJEITADA'));
                    END IF;
                END $$;
                """);
            log.info("Constraint reserva_sem_sobreposicao aplicada");
        };
    }
}
