package br.edu.senac.recursos;

import br.edu.senac.recursos.suporte.BaseIntegracaoTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CoberturaExtraTest extends BaseIntegracaoTest {

    @Test
    void tokenMalformadoCaiNoCatchDoJwtFiltro() throws Exception {
        mvc.perform(get("/salas").header("Authorization", "Bearer abc.invalid.xyz"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void headerSemBearerNaoAutentica() throws Exception {
        mvc.perform(get("/salas").header("Authorization", "Token abc"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void registroSemPerfilRetorna400ComErro() throws Exception {
        mvc.perform(post("/auth/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"novo@x.com\",\"senha\":\"1\",\"nome\":\"X\"}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.erro").exists());
    }

    @Test
    void loginEmailInvalidoRetorna400ComErro() throws Exception {
        mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"nao-e-email\",\"senha\":\"1\"}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.erro").exists());
    }

    @Test
    void salaSemNomeRetorna400ComErro() throws Exception {
        mvc.perform(post("/salas")
                .header("Authorization", "Bearer " + tokenAdm)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"capacidade\":10,\"localizacao\":\"X\"}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.erro").exists());
    }

    @Test
    void materialSemTipoRetorna400ComErro() throws Exception {
        mvc.perform(post("/materiais")
                .header("Authorization", "Bearer " + tokenAdm)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"X\",\"quantidade\":1}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.erro").exists());
    }

    @Test
    void professorSemEmailRetorna400ComErro() throws Exception {
        mvc.perform(post("/professores")
                .header("Authorization", "Bearer " + tokenAdm)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"X\",\"competencia\":\"Y\"}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.erro").exists());
    }
}
