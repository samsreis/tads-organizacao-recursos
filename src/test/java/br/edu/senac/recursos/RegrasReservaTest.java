package br.edu.senac.recursos;

import br.edu.senac.recursos.domain.Sala;
import br.edu.senac.recursos.suporte.BaseIntegracaoTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RegrasReservaTest extends BaseIntegracaoTest {

    @Test
    void sobreposicaoTotalRetorna409() throws Exception {
        criar("2026-12-01T10:00:00-03:00", "2026-12-01T11:00:00-03:00").andExpect(status().isCreated());
        criar("2026-12-01T10:00:00-03:00", "2026-12-01T11:00:00-03:00").andExpect(status().isConflict());
    }

    @Test
    void sobreposicaoParcialNoInicioRetorna409() throws Exception {
        criar("2026-12-01T10:00:00-03:00", "2026-12-01T11:00:00-03:00").andExpect(status().isCreated());
        criar("2026-12-01T09:30:00-03:00", "2026-12-01T10:30:00-03:00").andExpect(status().isConflict());
    }

    @Test
    void sobreposicaoParcialNoFimRetorna409() throws Exception {
        criar("2026-12-01T10:00:00-03:00", "2026-12-01T11:00:00-03:00").andExpect(status().isCreated());
        criar("2026-12-01T10:30:00-03:00", "2026-12-01T11:30:00-03:00").andExpect(status().isConflict());
    }

    @Test
    void horariosSemSobreposicaoAceitos() throws Exception {
        criar("2026-12-01T10:00:00-03:00", "2026-12-01T11:00:00-03:00").andExpect(status().isCreated());
        criar("2026-12-01T11:00:00-03:00", "2026-12-01T12:00:00-03:00").andExpect(status().isCreated());
    }

    @Test
    void salaEmManutencaoRetorna409() throws Exception {
        Sala s = new Sala();
        s.setNome("Manut");
        s.setCapacidade(10);
        s.setLocalizacao("Z");
        s.setEmManutencao(true);
        s = salas.save(s);
        mvc.perform(post("/reservas")
                .header("Authorization", "Bearer " + tokenSol)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"salaId\":" + s.getId() + ",\"inicio\":\"2026-12-01T10:00:00-03:00\",\"fim\":\"2026-12-01T11:00:00-03:00\"}"))
            .andExpect(status().isConflict());
    }

    @Test
    void salaNaoExistenteRetorna404() throws Exception {
        mvc.perform(post("/reservas")
                .header("Authorization", "Bearer " + tokenSol)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"salaId\":99999,\"inicio\":\"2026-12-01T10:00:00-03:00\",\"fim\":\"2026-12-01T11:00:00-03:00\"}"))
            .andExpect(status().isNotFound());
    }

    private ResultActions criar(String inicio, String fim) throws Exception {
        return mvc.perform(post("/reservas")
                .header("Authorization", "Bearer " + tokenSol)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"salaId\":" + salaId + ",\"inicio\":\"" + inicio + "\",\"fim\":\"" + fim + "\"}"));
    }
}
