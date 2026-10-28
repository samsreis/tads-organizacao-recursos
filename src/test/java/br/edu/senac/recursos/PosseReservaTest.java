package br.edu.senac.recursos;

import br.edu.senac.recursos.suporte.BaseIntegracaoTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PosseReservaTest extends BaseIntegracaoTest {

    @Test
    void donoCancelaPropriaReserva204() throws Exception {
        Long id = reservarComo("sol@teste.com");
        mvc.perform(delete("/reservas/" + id)
                .header("Authorization", "Bearer " + tokenSol))
            .andExpect(status().isNoContent());
    }

    @Test
    void outroSolicitanteNaoPodeCancelarReservaAlheia403() throws Exception {
        Long id = reservarComo("sol@teste.com");
        String outro = jwt.gerar("sol2@teste.com", "SOLICITANTE");
        mvc.perform(delete("/reservas/" + id)
                .header("Authorization", "Bearer " + outro))
            .andExpect(status().isForbidden());
    }

    private Long reservarComo(String email) throws Exception {
        MvcResult r = mvc.perform(post("/reservas")
                .header("Authorization", "Bearer " + jwt.gerar(email, "SOLICITANTE"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"salaId\":" + salaId + ",\"inicio\":\"2026-12-01T10:00:00-03:00\",\"fim\":\"2026-12-01T11:00:00-03:00\"}"))
            .andExpect(status().isCreated())
            .andReturn();
        Matcher m = Pattern.compile("\"id\":(\\d+)").matcher(r.getResponse().getContentAsString());
        if (!m.find()) throw new IllegalStateException("resposta sem id");
        return Long.parseLong(m.group(1));
    }
}
