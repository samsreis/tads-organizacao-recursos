package br.edu.senac.recursos;

import br.edu.senac.recursos.suporte.BaseIntegracaoTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SalaControllerBranchTest extends BaseIntegracaoTest {

    @Test
    void criarSalaComManutencaoFalseExplicita() throws Exception {
        mvc.perform(post("/salas")
                .header("Authorization", "Bearer " + tokenAdm)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"S1\",\"capacidade\":10,\"localizacao\":\"X\",\"emManutencao\":false}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.emManutencao").value(false));
    }

    @Test
    void criarSalaSemManutencaoCampo() throws Exception {
        mvc.perform(post("/salas")
                .header("Authorization", "Bearer " + tokenAdm)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"S2\",\"capacidade\":10,\"localizacao\":\"X\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.emManutencao").value(false));
    }

    @Test
    void atualizarSalaComManutencaoTrue() throws Exception {
        mvc.perform(put("/salas/" + salaId)
                .header("Authorization", "Bearer " + tokenAdm)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Sala Base\",\"capacidade\":30,\"localizacao\":\"Bloco A\",\"emManutencao\":true}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.emManutencao").value(true));
    }

    @Test
    void atualizarSalaComManutencaoFalse() throws Exception {
        mvc.perform(put("/salas/" + salaId)
                .header("Authorization", "Bearer " + tokenAdm)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Sala Base\",\"capacidade\":30,\"localizacao\":\"Bloco A\",\"emManutencao\":false}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.emManutencao").value(false));
    }
}
