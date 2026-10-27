package br.edu.senac.recursos;

import br.edu.senac.recursos.domain.Perfil;
import br.edu.senac.recursos.domain.Reserva;
import br.edu.senac.recursos.domain.Sala;
import br.edu.senac.recursos.domain.Usuario;
import br.edu.senac.recursos.repository.ReservaRepository;
import br.edu.senac.recursos.repository.SalaRepository;
import br.edu.senac.recursos.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
class SobreposicaoTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("recursos")
            .withUsername("recursos")
            .withPassword("recursos");

    @DynamicPropertySource
    static void datasource(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", postgres::getJdbcUrl);
        r.add("spring.datasource.username", postgres::getUsername);
        r.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired ReservaRepository reservas;
    @Autowired SalaRepository salas;
    @Autowired UsuarioRepository usuarios;

    @Test
    void bancoBloqueiaSobreposicaoNaMesmaSala() {
        Sala sala = new Sala();
        sala.setNome("Sala Teste");
        sala.setCapacidade(20);
        sala.setLocalizacao("Bloco B");
        sala.setEmManutencao(false);
        salas.save(sala);

        Usuario u = new Usuario();
        u.setEmail("teste@teste.com");
        u.setSenhaHash("x");
        u.setNome("Teste");
        u.setPerfil(Perfil.SOLICITANTE);
        usuarios.save(u);

        Reserva r1 = new Reserva();
        r1.setSala(sala);
        r1.setSolicitante(u);
        r1.setInicio(OffsetDateTime.parse("2026-12-01T10:00:00-03:00"));
        r1.setFim(OffsetDateTime.parse("2026-12-01T11:00:00-03:00"));
        r1.setStatus(Reserva.StatusReserva.APROVADA);
        reservas.saveAndFlush(r1);

        Reserva r2 = new Reserva();
        r2.setSala(sala);
        r2.setSolicitante(u);
        r2.setInicio(OffsetDateTime.parse("2026-12-01T10:30:00-03:00"));
        r2.setFim(OffsetDateTime.parse("2026-12-01T11:30:00-03:00"));
        r2.setStatus(Reserva.StatusReserva.APROVADA);

        Exception e = assertThrows(Exception.class, () -> reservas.saveAndFlush(r2));
        assertThat(e.getMessage().toLowerCase()).contains("reserva_sem_sobreposicao");
    }
}
