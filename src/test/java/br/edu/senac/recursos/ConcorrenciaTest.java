package br.edu.senac.recursos;

import br.edu.senac.recursos.domain.Perfil;
import br.edu.senac.recursos.domain.Professor;
import br.edu.senac.recursos.domain.Sala;
import br.edu.senac.recursos.domain.Usuario;
import br.edu.senac.recursos.repository.ProfessorRepository;
import br.edu.senac.recursos.repository.ReservaRepository;
import br.edu.senac.recursos.repository.SalaRepository;
import br.edu.senac.recursos.repository.UsuarioRepository;
import br.edu.senac.recursos.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConcorrenciaTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("recursos")
            .withUsername("recursos")
            .withPassword("recursos");

    static {
        postgres.start();
    }

    @DynamicPropertySource
    static void datasource(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", postgres::getJdbcUrl);
        r.add("spring.datasource.username", postgres::getUsername);
        r.add("spring.datasource.password", postgres::getPassword);
        r.add("spring.datasource.hikari.maximum-pool-size", () -> "25");
    }

    @Autowired TestRestTemplate http;
    @Autowired JwtService jwt;
    @Autowired SalaRepository salas;
    @Autowired ProfessorRepository professores;
    @Autowired UsuarioRepository usuarios;
    @Autowired ReservaRepository reservas;
    @Autowired PasswordEncoder encoder;

    Long salaId;
    String token;

    @BeforeEach
    void preparar() {
        if (usuarios.findByEmail("conc@teste.com").isEmpty()) {
            Usuario u = new Usuario();
            u.setEmail("conc@teste.com");
            u.setSenhaHash(encoder.encode("123456"));
            u.setNome("Concorrente");
            u.setPerfil(Perfil.SOLICITANTE);
            usuarios.save(u);
        }
        Sala s = new Sala();
        s.setNome("Sala Conc " + System.nanoTime());
        s.setCapacidade(20);
        s.setLocalizacao("Bloco C");
        s.setEmManutencao(false);
        s = salas.save(s);
        salaId = s.getId();
        token = jwt.gerar("conc@teste.com", "SOLICITANTE");
    }

    @Test
    void apenasUmaReservaAceitaEmDezSolicitacoesSimultaneas() throws Exception {
        int n = 10;
        ExecutorService pool = Executors.newFixedThreadPool(n);
        CountDownLatch largada = new CountDownLatch(1);
        List<Future<Integer>> futuros = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            futuros.add(pool.submit(() -> {
                largada.await();
                HttpHeaders h = new HttpHeaders();
                h.setContentType(MediaType.APPLICATION_JSON);
                h.setBearerAuth(token);
                String body = "{\"salaId\":" + salaId
                        + ",\"inicio\":\"2026-12-02T10:00:00-03:00\""
                        + ",\"fim\":\"2026-12-02T11:00:00-03:00\"}";
                ResponseEntity<String> resp = http.postForEntity("/reservas", new HttpEntity<>(body, h), String.class);
                return resp.getStatusCode().value();
            }));
        }
        largada.countDown();

        AtomicInteger aceitas = new AtomicInteger();
        AtomicInteger conflitos = new AtomicInteger();
        AtomicInteger outros = new AtomicInteger();
        for (Future<Integer> f : futuros) {
            int st = f.get(60, TimeUnit.SECONDS);
            if (st == 201) aceitas.incrementAndGet();
            else if (st == 409) conflitos.incrementAndGet();
            else outros.incrementAndGet();
        }
        pool.shutdown();

        assertThat(aceitas.get()).as("exatamente uma aceita").isEqualTo(1);
        assertThat(conflitos.get()).as("resto vira 409").isEqualTo(n - 1);
        assertThat(outros.get()).as("sem erros inesperados").isZero();
        long gravadas = reservas.findAll().stream()
                .filter(r -> r.getSala().getId().equals(salaId))
                .count();
        assertThat(gravadas).as("uma unica reserva no banco").isEqualTo(1L);
    }

    @Test
    void apenasUmaReservaAceitaParaProfessorEmSolicitacoesSimultaneas() throws Exception {
        int n = 10;
        Professor professor = new Professor();
        professor.setNome("Professor Concorrente");
        professor.setEmail("prof.concorrente@teste.com");
        professor.setCompetencia("Qualidade");
        professor.setAtivo(true);
        professor = professores.save(professor);
        Long professorId = professor.getId();

        List<Long> salasIds = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Sala sala = new Sala();
            sala.setNome("Sala Professor Conc " + i + " " + System.nanoTime());
            sala.setCapacidade(20);
            sala.setLocalizacao("Bloco P");
            sala.setEmManutencao(false);
            salasIds.add(salas.save(sala).getId());
        }

        ExecutorService pool = Executors.newFixedThreadPool(n);
        CountDownLatch largada = new CountDownLatch(1);
        List<Future<Integer>> futuros = new ArrayList<>();
        for (Long idSala : salasIds) {
            futuros.add(pool.submit(() -> {
                largada.await();
                HttpHeaders h = new HttpHeaders();
                h.setContentType(MediaType.APPLICATION_JSON);
                h.setBearerAuth(token);
                String body = "{\"salaId\":" + idSala
                        + ",\"professorId\":" + professorId
                        + ",\"inicio\":\"2026-12-05T10:00:00-03:00\""
                        + ",\"fim\":\"2026-12-05T11:00:00-03:00\"}";
                ResponseEntity<String> resp = http.postForEntity("/reservas", new HttpEntity<>(body, h), String.class);
                return resp.getStatusCode().value();
            }));
        }
        largada.countDown();

        AtomicInteger aceitas = new AtomicInteger();
        AtomicInteger conflitos = new AtomicInteger();
        AtomicInteger outros = new AtomicInteger();
        try {
            for (Future<Integer> futuro : futuros) {
                int status = futuro.get(60, TimeUnit.SECONDS);
                if (status == 201) aceitas.incrementAndGet();
                else if (status == 409) conflitos.incrementAndGet();
                else outros.incrementAndGet();
            }
        } finally {
            pool.shutdownNow();
        }

        assertThat(aceitas.get()).as("exatamente uma reserva para o professor").isEqualTo(1);
        assertThat(conflitos.get()).as("conflitos restantes para o professor").isEqualTo(n - 1);
        assertThat(outros.get()).as("sem erros inesperados").isZero();
        long gravadas = reservas.findAll().stream()
                .filter(r -> r.getProfessor() != null && r.getProfessor().getId().equals(professorId))
                .count();
        assertThat(gravadas).as("uma unica reserva de professor no banco").isEqualTo(1L);
    }
}
