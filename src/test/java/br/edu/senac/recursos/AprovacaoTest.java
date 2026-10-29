package br.edu.senac.recursos;

import br.edu.senac.recursos.suporte.BaseIntegracaoTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AprovacaoTest extends BaseIntegracaoTest {

    @Test
    void responsavelAprovaReserva201() throws Exception {
        Long id = reservar("2026-12-10T10:00:00-03:00", "2026-12-10T11:00:00-03:00");
        mvc.perform(post("/reservas/" + id + "/aprovar")
                .header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("APROVADA"));
    }

    @Test
    void administradorAprovaReserva() throws Exception {
        Long id = reservar("2026-12-10T10:00:00-03:00", "2026-12-10T11:00:00-03:00");
        mvc.perform(post("/reservas/" + id + "/aprovar")
                .header("Authorization", "Bearer " + tokenAdm))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("APROVADA"));
    }

    @Test
    void solicitanteNaoPodeAprovar403() throws Exception {
        Long id = reservar("2026-12-10T10:00:00-03:00", "2026-12-10T11:00:00-03:00");
        mvc.perform(post("/reservas/" + id + "/aprovar")
                .header("Authorization", "Bearer " + tokenSol))
            .andExpect(status().isForbidden());
    }

    @Test
    void anonimoNaoPodeAprovar401() throws Exception {
        Long id = reservar("2026-12-10T10:00:00-03:00", "2026-12-10T11:00:00-03:00");
        mvc.perform(post("/reservas/" + id + "/aprovar"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void naoAprovaReservaJaAprovada409() throws Exception {
        Long id = reservar("2026-12-10T10:00:00-03:00", "2026-12-10T11:00:00-03:00");
        mvc.perform(post("/reservas/" + id + "/aprovar").header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isOk());
        mvc.perform(post("/reservas/" + id + "/aprovar").header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isConflict());
    }

    @Test
    void aprovarReservaInexistenteRetorna404() throws Exception {
        mvc.perform(post("/reservas/99999/aprovar").header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isNotFound());
    }

    @Test
    void cancelarReservaIiniciadaRetorna409() throws Exception {
        Long id = reservar("2020-01-01T10:00:00-03:00", "2020-01-01T11:00:00-03:00");
        mvc.perform(delete("/reservas/" + id).header("Authorization", "Bearer " + tokenSol))
            .andExpect(status().isConflict());
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
