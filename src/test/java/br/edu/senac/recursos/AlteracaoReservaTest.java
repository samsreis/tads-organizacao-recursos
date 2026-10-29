package br.edu.senac.recursos;

import br.edu.senac.recursos.suporte.BaseIntegracaoTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AlteracaoReservaTest extends BaseIntegracaoTest {

    @Test
    void donoAlteraPropriaReserva200() throws Exception {
        Long id = reservar("2026-12-15T10:00:00-03:00", "2026-12-15T11:00:00-03:00");
        mvc.perform(put("/reservas/" + id)
                .header("Authorization", "Bearer " + tokenSol)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"salaId\":" + salaId + ",\"inicio\":\"2026-12-15T14:00:00-03:00\",\"fim\":\"2026-12-15T15:00:00-03:00\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.inicio").exists());
    }

    @Test
    void outroSolicitanteNaoPodeAlterar403() throws Exception {
        Long id = reservar("2026-12-15T10:00:00-03:00", "2026-12-15T11:00:00-03:00");
        String outro = jwt.gerar("sol2@teste.com", "SOLICITANTE");
        mvc.perform(put("/reservas/" + id)
                .header("Authorization", "Bearer " + outro)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"salaId\":" + salaId + ",\"inicio\":\"2026-12-15T14:00:00-03:00\",\"fim\":\"2026-12-15T15:00:00-03:00\"}"))
            .andExpect(status().isForbidden());
    }

    @Test
    void reservaIiniciadaNaoPodeSerAlterada409() throws Exception {
        Long id = reservar("2020-01-01T10:00:00-03:00", "2020-01-01T11:00:00-03:00");
        mvc.perform(put("/reservas/" + id)
                .header("Authorization", "Bearer " + tokenSol)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"salaId\":" + salaId + ",\"inicio\":\"2026-12-15T14:00:00-03:00\",\"fim\":\"2026-12-15T15:00:00-03:00\"}"))
            .andExpect(status().isConflict());
    }

    @Test
    void alterarParaHorarioEmConflito409() throws Exception {
        reservar("2026-12-20T10:00:00-03:00", "2026-12-20T11:00:00-03:00");
        Long id2 = reservar("2026-12-20T14:00:00-03:00", "2026-12-20T15:00:00-03:00");
        mvc.perform(put("/reservas/" + id2)
                .header("Authorization", "Bearer " + tokenSol)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"salaId\":" + salaId + ",\"inicio\":\"2026-12-20T10:00:00-03:00\",\"fim\":\"2026-12-20T11:00:00-03:00\"}"))
            .andExpect(status().isConflict());
    }

    @Test
    void alterarReservaInexistente404() throws Exception {
        mvc.perform(put("/reservas/99999")
                .header("Authorization", "Bearer " + tokenSol)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"salaId\":" + salaId + ",\"inicio\":\"2026-12-20T10:00:00-03:00\",\"fim\":\"2026-12-20T11:00:00-03:00\"}"))
            .andExpect(status().isNotFound());
    }

    @Test
    void terminoAntesDoInicio400() throws Exception {
        Long id = reservar("2026-12-21T10:00:00-03:00", "2026-12-21T11:00:00-03:00");
        mvc.perform(put("/reservas/" + id)
                .header("Authorization", "Bearer " + tokenSol)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"salaId\":" + salaId + ",\"inicio\":\"2026-12-21T11:00:00-03:00\",\"fim\":\"2026-12-21T10:00:00-03:00\"}"))
            .andExpect(status().isBadRequest());
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
