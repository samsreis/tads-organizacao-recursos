package br.edu.senac.recursos;

import br.edu.senac.recursos.suporte.BaseIntegracaoTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SalaControllerTest extends BaseIntegracaoTest {

    @Test
    void criarSalaComManutencaoTrueAceita() throws Exception {
        mvc.perform(post("/salas")
                .header("Authorization", "Bearer " + tokenAdm)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Oficina\",\"capacidade\":8,\"localizacao\":\"Z\",\"emManutencao\":true}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.emManutencao").value(true));
    }

    @Test
    void atualizarSalaInexistenteRetorna404() throws Exception {
        mvc.perform(put("/salas/99999")
                .header("Authorization", "Bearer " + tokenAdm)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"X\",\"capacidade\":10,\"localizacao\":\"Y\"}"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.erro").exists());
    }

    @Test
    void buscarSalaInexistenteRetorna404() throws Exception {
        mvc.perform(get("/salas/99999")
                .header("Authorization", "Bearer " + tokenSol))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.erro").exists());
    }
}
