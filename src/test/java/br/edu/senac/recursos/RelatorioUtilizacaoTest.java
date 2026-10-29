package br.edu.senac.recursos;

import br.edu.senac.recursos.suporte.BaseIntegracaoTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RelatorioUtilizacaoTest extends BaseIntegracaoTest {

    @Test
    void responsavelVeRelatorio() throws Exception {
        reservar("2026-12-30T10:00:00-03:00", "2026-12-30T12:00:00-03:00");
        mvc.perform(get("/relatorios/utilizacao?inicio=2026-12-30T00:00:00-03:00&fim=2026-12-31T00:00:00-03:00")
                .header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.porSala").isArray())
            .andExpect(jsonPath("$.porProfessor").isArray())
            .andExpect(jsonPath("$.conflitosEvitados").exists());
    }

    @Test
    void solicitanteNaoVeRelatorio403() throws Exception {
        mvc.perform(get("/relatorios/utilizacao?inicio=2026-12-30T00:00:00-03:00&fim=2026-12-31T00:00:00-03:00")
                .header("Authorization", "Bearer " + tokenSol))
            .andExpect(status().isForbidden());
    }

    @Test
    void anonimoNaoVeRelatorio401() throws Exception {
        mvc.perform(get("/relatorios/utilizacao?inicio=2026-12-30T00:00:00-03:00&fim=2026-12-31T00:00:00-03:00"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void conflitoEvitadoApareceNoRelatorio() throws Exception {
        reservar("2026-12-31T10:00:00-03:00", "2026-12-31T11:00:00-03:00");
        mvc.perform(post("/reservas")
                .header("Authorization", "Bearer " + tokenSol)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"salaId\":" + salaId + ",\"inicio\":\"2026-12-31T10:30:00-03:00\",\"fim\":\"2026-12-31T11:30:00-03:00\"}"))
            .andExpect(status().isConflict());
        mvc.perform(get("/relatorios/utilizacao?inicio=2026-12-31T00:00:00-03:00&fim=2027-01-01T00:00:00-03:00")
                .header("Authorization", "Bearer " + tokenAdm))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.conflitosEvitados").value(1));
    }

    private Long reservar(String ini, String fim) throws Exception {
        MvcResult r = mvc.perform(post("/reservas")
                .header("Authorization", "Bearer " + tokenSol)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"salaId\":" + salaId + ",\"inicio\":\"" + ini + "\",\"fim\":\"" + fim + "\"}"))
            .andExpect(status().isCreated()).andReturn();
        Matcher m = Pattern.compile("\"id\":(\\d+)").matcher(r.getResponse().getContentAsString());
        if (!m.find()) throw new IllegalStateException("resposta sem id");
        return Long.parseLong(m.group(1));
    }
}
