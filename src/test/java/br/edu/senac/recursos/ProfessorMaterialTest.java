package br.edu.senac.recursos;

import br.edu.senac.recursos.suporte.BaseIntegracaoTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfessorMaterialTest extends BaseIntegracaoTest {

    @Test
    void adminCriaProfessor201() throws Exception {
        criarProfessor("Joao", "joao@senac.br", "Java").andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.nome").value("Joao"));
    }

    @Test
    void solicitanteNaoCriaProfessor403() throws Exception {
        mvc.perform(post("/professores")
                .header("Authorization", "Bearer " + tokenSol)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"X\",\"email\":\"x@x.com\",\"competencia\":\"Y\"}"))
            .andExpect(status().isForbidden());
    }

    @Test
    void anonimoNaoListaProfessores401() throws Exception {
        mvc.perform(get("/professores")).andExpect(status().isUnauthorized());
    }

    @Test
    void autenticadoListaProfessores200() throws Exception {
        criarProfessor("Maria", "maria@senac.br", "Banco de Dados").andExpect(status().isCreated());
        mvc.perform(get("/professores").header("Authorization", "Bearer " + tokenSol))
            .andExpect(status().isOk());
    }

    @Test
    void filtroPorCompetencia() throws Exception {
        criarProfessor("Ana", "ana@senac.br", "Redes").andExpect(status().isCreated());
        mvc.perform(get("/professores?competencia=redes")
                .header("Authorization", "Bearer " + tokenSol))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].competencia").value("Redes"));
    }

    @Test
    void emailDuplicadoProfessor409() throws Exception {
        criarProfessor("Pedro", "pedro@senac.br", "Java").andExpect(status().isCreated());
        criarProfessor("Pedro2", "pedro@senac.br", "Python").andExpect(status().isConflict());
    }

    @Test
    void adminCriaMaterial201() throws Exception {
        mvc.perform(post("/materiais")
                .header("Authorization", "Bearer " + tokenAdm)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Projetor\",\"tipo\":\"Eletronico\",\"quantidade\":3}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void solicitanteNaoCriaMaterial403() throws Exception {
        mvc.perform(post("/materiais")
                .header("Authorization", "Bearer " + tokenSol)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"X\",\"tipo\":\"Y\",\"quantidade\":1}"))
            .andExpect(status().isForbidden());
    }

    @Test
    void filtroMaterialPorTipo() throws Exception {
        mvc.perform(post("/materiais")
                .header("Authorization", "Bearer " + tokenAdm)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Cabos\",\"tipo\":\"Rede\",\"quantidade\":10}"))
            .andExpect(status().isCreated());
        mvc.perform(get("/materiais?tipo=rede")
                .header("Authorization", "Bearer " + tokenSol))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].tipo").value("Rede"));
    }

    @Test
    void materialEmManutencaoRetorna200() throws Exception {
        mvc.perform(post("/materiais")
                .header("Authorization", "Bearer " + tokenAdm)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Quebrado\",\"tipo\":\"X\",\"quantidade\":1,\"emManutencao\":true}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.emManutencao").value(true));
    }

    private ResultActions criarProfessor(String nome, String email, String competencia) throws Exception {
        return mvc.perform(post("/professores")
                .header("Authorization", "Bearer " + tokenAdm)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"" + nome + "\",\"email\":\"" + email + "\",\"competencia\":\"" + competencia + "\"}"));
    }
}
