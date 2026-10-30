package br.edu.senac.recursos;

import br.edu.senac.recursos.domain.Reserva;
import br.edu.senac.recursos.domain.Sala;
import br.edu.senac.recursos.domain.Usuario;
import br.edu.senac.recursos.suporte.BaseIntegracaoTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FiltroSalaTest extends BaseIntegracaoTest {

    private Long salaPequena;
    private Long salaMedia;
    private Long salaGrande;

    @BeforeEach
    void prepararSalas() {
        salas.deleteAll();
        salaPequena = criarSala("Sala F-01", 10, "Bloco F");
        salaMedia = criarSala("Sala F-02", 30, "Bloco F");
        salaGrande = criarSala("Sala G-01", 60, "Bloco G");
    }

    private Long criarSala(String nome, int capacidade, String local) {
        Sala s = new Sala();
        s.setNome(nome);
        s.setCapacidade(capacidade);
        s.setLocalizacao(local);
        s.setEmManutencao(false);
        return salas.save(s).getId();
    }

    @Test
    void filtraPorCapacidadeMinima() throws Exception {
        mvc.perform(get("/salas").param("capacidadeMinima", "20")
                .header("Authorization", "Bearer " + tokenSol))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void filtraPorLocalizacao() throws Exception {
        mvc.perform(get("/salas").param("localizacao", "G")
                .header("Authorization", "Bearer " + tokenSol))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(salaGrande));
    }

    @Test
    void combinaCapacidadeELocalizacao() throws Exception {
        mvc.perform(get("/salas")
                .param("capacidadeMinima", "20").param("localizacao", "Bloco F")
                .header("Authorization", "Bearer " + tokenSol))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(salaMedia));
    }

    @Test
    void filtraPorDisponibilidade() throws Exception {
        OffsetDateTime inicio = OffsetDateTime.parse("2026-12-01T10:00:00-03:00");
        OffsetDateTime fim = OffsetDateTime.parse("2026-12-01T11:00:00-03:00");

        Usuario u = usuarios.findByEmail("sol@teste.com").orElseThrow();
        Reserva r = new Reserva();
        r.setSala(salas.findById(salaMedia).orElseThrow());
        r.setSolicitante(u);
        r.setInicio(inicio);
        r.setFim(fim);
        r.setStatus(Reserva.StatusReserva.APROVADA);
        reservas.save(r);

        mvc.perform(get("/salas")
                .param("inicio", inicio.toString()).param("fim", fim.toString())
                .header("Authorization", "Bearer " + tokenSol))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void reservaCanceladaNaoOcupa() throws Exception {
        OffsetDateTime inicio = OffsetDateTime.parse("2026-12-02T10:00:00-03:00");
        OffsetDateTime fim = OffsetDateTime.parse("2026-12-02T11:00:00-03:00");

        Usuario u = usuarios.findByEmail("sol@teste.com").orElseThrow();
        Reserva r = new Reserva();
        r.setSala(salas.findById(salaMedia).orElseThrow());
        r.setSolicitante(u);
        r.setInicio(inicio);
        r.setFim(fim);
        r.setStatus(Reserva.StatusReserva.CANCELADA);
        reservas.save(r);

        mvc.perform(get("/salas")
                .param("inicio", inicio.toString()).param("fim", fim.toString())
                .header("Authorization", "Bearer " + tokenSol))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void intervaloInvalidoRetornaErro() throws Exception {
        mvc.perform(get("/salas")
                .param("inicio", "2026-12-01T11:00:00-03:00")
                .param("fim", "2026-12-01T10:00:00-03:00")
                .header("Authorization", "Bearer " + tokenSol))
                .andExpect(status().isBadRequest());
    }
}
