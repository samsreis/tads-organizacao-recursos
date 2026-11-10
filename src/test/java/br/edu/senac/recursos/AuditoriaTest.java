package br.edu.senac.recursos;

import br.edu.senac.recursos.suporte.BaseIntegracaoTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuditoriaTest extends BaseIntegracaoTest {

    @Test
    void criacaoGeraRegistroDeAuditoria() throws Exception {
        Long id = reservar();
        mvc.perform(get("/reservas/" + id + "/historico")
                .header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].estadoNovo").value("SOLICITADA"))
            .andExpect(jsonPath("$[0].motivo").exists());
    }

    @Test
    void aprovacaoGeraSegundoRegistroComEstadoAnterior() throws Exception {
        Long id = reservar();
        mvc.perform(post("/reservas/" + id + "/aprovar").header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isOk());
        mvc.perform(get("/reservas/" + id + "/historico")
                .header("Authorization", "Bearer " + tokenAdm))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[1].estadoAnterior").value("SOLICITADA"))
            .andExpect(jsonPath("$[1].estadoNovo").value("APROVADA"));
    }

    @Test
    void cancelamentoGeraRegistro() throws Exception {
        Long id = reservar();
        mvc.perform(delete("/reservas/" + id).header("Authorization", "Bearer " + tokenSol))
            .andExpect(status().isNoContent());
        mvc.perform(get("/reservas/" + id + "/historico")
                .header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[1].estadoNovo").value("CANCELADA"));
    }

    @Test
    void solicitanteNaoPodeVerHistorico403() throws Exception {
        Long id = reservar();
        mvc.perform(get("/reservas/" + id + "/historico")
                .header("Authorization", "Bearer " + tokenSol))
            .andExpect(status().isForbidden());
    }

    @Test
    void anonimoNaoPodeVerHistorico401() throws Exception {
        Long id = reservar();
        mvc.perform(get("/reservas/" + id + "/historico"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void historicoDeReservaInexistente404() throws Exception {
        mvc.perform(get("/reservas/99999/historico").header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isNotFound());
    }

    // Teste de rollback real em AuditoriaRollbackTest.

    private Long reservar() throws Exception {
        MvcResult r = mvc.perform(post("/reservas")
                .header("Authorization", "Bearer " + tokenSol)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"salaId\":" + salaId + ",\"inicio\":\"2026-12-25T10:00:00-03:00\",\"fim\":\"2026-12-25T11:00:00-03:00\"}"))
            .andExpect(status().isCreated()).andReturn();
        Matcher m = Pattern.compile("\"id\":(\\d+)").matcher(r.getResponse().getContentAsString());
        if (!m.find()) throw new IllegalStateException("resposta sem id");
        return Long.parseLong(m.group(1));
    }
}
