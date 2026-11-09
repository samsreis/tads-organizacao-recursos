package br.edu.senac.recursos;

import br.edu.senac.recursos.suporte.BaseIntegracaoTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends BaseIntegracaoTest {

    @Test
    void registroNovoRetorna201() throws Exception {
        mvc.perform(post("/auth/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"novo@teste.com\",\"senha\":\"123456\",\"nome\":\"Novo\",\"perfil\":\"SOLICITANTE\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void registroEmailDuplicadoRetorna409() throws Exception {
        mvc.perform(post("/auth/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"sol@teste.com\",\"senha\":\"123456\",\"nome\":\"X\",\"perfil\":\"SOLICITANTE\"}"))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.erro").exists());
    }

    @Test
    void loginInexistenteRetorna401() throws Exception {
        mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"fantasma@teste.com\",\"senha\":\"123456\"}"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.erro").exists());
    }

    @Test
    void loginSenhaErradaRetorna401() throws Exception {
        mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"sol@teste.com\",\"senha\":\"errada\"}"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.erro").exists());
    }

    @Test
    void loginCorretoRetorna200ComToken() throws Exception {
        mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"sol@teste.com\",\"senha\":\"123456\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").exists())
            .andExpect(jsonPath("$.perfil").value("SOLICITANTE"));
    }

    @Test
    void cadastroComPerfilAdministradorGravaSolicitante() throws Exception {
        mvc.perform(post("/auth/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"tentativaadm@teste.com\",\"senha\":\"123456\",\"nome\":\"Tentativa\",\"perfil\":\"ADMINISTRADOR\"}"))
            .andExpect(status().isCreated());

        var criado = usuarios.findByEmail("tentativaadm@teste.com").orElseThrow();
        org.junit.jupiter.api.Assertions.assertEquals(
            br.edu.senac.recursos.domain.Perfil.SOLICITANTE, criado.getPerfil());
    }
}
