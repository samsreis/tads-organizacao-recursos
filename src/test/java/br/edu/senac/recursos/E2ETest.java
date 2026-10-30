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

class E2ETest extends BaseIntegracaoTest {

    @Test
    void fluxoCompletoCadastroLoginReservaAprovacaoUsoConclusao() throws Exception {
        String email = "e2e" + System.nanoTime() + "@teste.com";

        mvc.perform(post("/auth/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"" + email + "\",\"senha\":\"123456\",\"nome\":\"E2E\",\"perfil\":\"SOLICITANTE\"}"))
            .andExpect(status().isCreated());

        MvcResult login = mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"" + email + "\",\"senha\":\"123456\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").exists())
            .andReturn();
        String token = extrair(login.getResponse().getContentAsString(), "\"token\":\"([^\"]+)\"");

        MvcResult criada = mvc.perform(post("/reservas")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"salaId\":" + salaId + ",\"inicio\":\"2027-01-10T10:00:00-03:00\",\"fim\":\"2027-01-10T11:00:00-03:00\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value("SOLICITADA"))
            .andReturn();
        Long id = Long.valueOf(extrair(criada.getResponse().getContentAsString(), "\"id\":(\\d+)"));

        mvc.perform(post("/reservas/" + id + "/aprovar").header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("APROVADA"));

        mvc.perform(post("/reservas/" + id + "/iniciar").header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("EM_USO"));

        mvc.perform(post("/reservas/" + id + "/concluir").header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("CONCLUIDA"));

        mvc.perform(get("/reservas/" + id + "/historico").header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isOk());
    }

    private String extrair(String texto, String regex) {
        Matcher m = Pattern.compile(regex).matcher(texto);
        if (!m.find()) throw new IllegalStateException("nao encontrou: " + regex);
        return m.group(1);
    }
}
