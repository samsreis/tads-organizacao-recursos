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

class MaquinaEstadosTest extends BaseIntegracaoTest {

    @Test
    void aprovadaParaEmUsoPeloResponsavel() throws Exception {
        Long id = reservarAprovada("2026-12-10T10:00:00-03:00", "2026-12-10T11:00:00-03:00");
        mvc.perform(post("/reservas/" + id + "/iniciar").header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("EM_USO"));
    }

    @Test
    void solicitanteNaoPodeIniciar403() throws Exception {
        Long id = reservarAprovada("2026-12-10T10:00:00-03:00", "2026-12-10T11:00:00-03:00");
        mvc.perform(post("/reservas/" + id + "/iniciar").header("Authorization", "Bearer " + tokenSol))
            .andExpect(status().isForbidden());
    }

    @Test
    void anonimoNaoPodeIniciar401() throws Exception {
        Long id = reservarAprovada("2026-12-10T10:00:00-03:00", "2026-12-10T11:00:00-03:00");
        mvc.perform(post("/reservas/" + id + "/iniciar"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void emUsoParaConcluida() throws Exception {
        Long id = reservarAprovada("2026-12-10T10:00:00-03:00", "2026-12-10T11:00:00-03:00");
        mvc.perform(post("/reservas/" + id + "/iniciar").header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isOk());
        mvc.perform(post("/reservas/" + id + "/concluir").header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("CONCLUIDA"));
    }

    @Test
    void solicitadaNaoPodeIrDiretoParaConcluida409() throws Exception {
        Long id = reservar("2026-12-10T10:00:00-03:00", "2026-12-10T11:00:00-03:00");
        mvc.perform(post("/reservas/" + id + "/concluir").header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isConflict());
    }

    @Test
    void solicitadaNaoPodeIrDiretoParaEmUso409() throws Exception {
        Long id = reservar("2026-12-10T10:00:00-03:00", "2026-12-10T11:00:00-03:00");
        mvc.perform(post("/reservas/" + id + "/iniciar").header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isConflict());
    }

    @Test
    void responsavelRejeitaSolicitada() throws Exception {
        Long id = reservar("2026-12-10T10:00:00-03:00", "2026-12-10T11:00:00-03:00");
        mvc.perform(post("/reservas/" + id + "/rejeitar")
                .header("Authorization", "Bearer " + tokenRes)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"motivo\":\"Sala em obra\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("REJEITADA"));
    }

    @Test
    void rejeitadaNaoVoltaParaAprovada409() throws Exception {
        Long id = reservar("2026-12-10T10:00:00-03:00", "2026-12-10T11:00:00-03:00");
        mvc.perform(post("/reservas/" + id + "/rejeitar").header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isOk());
        mvc.perform(post("/reservas/" + id + "/aprovar").header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isConflict());
    }

    @Test
    void aprovadaParaNaoCompareceu() throws Exception {
        Long id = reservarAprovada("2026-12-10T10:00:00-03:00", "2026-12-10T11:00:00-03:00");
        mvc.perform(post("/reservas/" + id + "/nao-compareceu").header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("NAO_COMPARECEU"));
    }

    @Test
    void concluidaNaoAceitaNovaTransicao409() throws Exception {
        Long id = reservarAprovada("2026-12-10T10:00:00-03:00", "2026-12-10T11:00:00-03:00");
        mvc.perform(post("/reservas/" + id + "/iniciar").header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isOk());
        mvc.perform(post("/reservas/" + id + "/concluir").header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isOk());
        mvc.perform(post("/reservas/" + id + "/iniciar").header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isConflict());
    }

    @Test
    void transicaoEmReservaInexistente404() throws Exception {
        mvc.perform(post("/reservas/99999/iniciar").header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isNotFound());
    }

    @Test
    void historicoRegistraCadaTransicao() throws Exception {
        Long id = reservarAprovada("2026-12-10T10:00:00-03:00", "2026-12-10T11:00:00-03:00");
        mvc.perform(post("/reservas/" + id + "/iniciar").header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isOk());
        mvc.perform(post("/reservas/" + id + "/concluir").header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isOk());

        mvc.perform(get("/reservas/" + id + "/historico").header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(4))
            .andExpect(jsonPath("$[0].estadoNovo").value("SOLICITADA"))
            .andExpect(jsonPath("$[1].estadoNovo").value("APROVADA"))
            .andExpect(jsonPath("$[2].estadoNovo").value("EM_USO"))
            .andExpect(jsonPath("$[3].estadoNovo").value("CONCLUIDA"));
    }

    @Test
    void motivoPersonalizadoEhRegistrado() throws Exception {
        Long id = reservar("2026-12-10T10:00:00-03:00", "2026-12-10T11:00:00-03:00");
        mvc.perform(post("/reservas/" + id + "/rejeitar")
                .header("Authorization", "Bearer " + tokenRes)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"motivo\":\"Conflito com evento externo\"}"))
            .andExpect(status().isOk());

        mvc.perform(get("/reservas/" + id + "/historico").header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[1].motivo").value("Conflito com evento externo"));
    }

    private Long reservarAprovada(String ini, String fim) throws Exception {
        Long id = reservar(ini, fim);
        mvc.perform(post("/reservas/" + id + "/aprovar").header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isOk());
        return id;
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
