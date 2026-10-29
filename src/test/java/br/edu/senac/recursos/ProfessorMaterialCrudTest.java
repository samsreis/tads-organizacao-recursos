package br.edu.senac.recursos;

import br.edu.senac.recursos.suporte.BaseIntegracaoTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfessorMaterialCrudTest extends BaseIntegracaoTest {

    @Test
    void professorBuscarInexistenteRetorna404() throws Exception {
        mvc.perform(get("/professores/99999").header("Authorization", "Bearer " + tokenSol))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.erro").exists());
    }

    @Test
    void professorBuscarExistenteRetorna200() throws Exception {
        Long id = criarProfessor("P1", "p1@x.com", "Java");
        mvc.perform(get("/professores/" + id).header("Authorization", "Bearer " + tokenSol))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void professorAtualizarExistenteRetorna200() throws Exception {
        Long id = criarProfessor("P2", "p2@x.com", "Java");
        mvc.perform(put("/professores/" + id)
                .header("Authorization", "Bearer " + tokenAdm)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"P2 novo\",\"email\":\"p2@x.com\",\"competencia\":\"Python\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome").value("P2 novo"))
            .andExpect(jsonPath("$.competencia").value("Python"));
    }

    @Test
    void professorAtualizarInexistenteRetorna404() throws Exception {
        mvc.perform(put("/professores/99999")
                .header("Authorization", "Bearer " + tokenAdm)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"X\",\"email\":\"x@x.com\",\"competencia\":\"Y\"}"))
            .andExpect(status().isNotFound());
    }

    @Test
    void professorApagarExistenteRetorna204() throws Exception {
        Long id = criarProfessor("P3", "p3@x.com", "Java");
        mvc.perform(delete("/professores/" + id).header("Authorization", "Bearer " + tokenAdm))
            .andExpect(status().isNoContent());
    }

    @Test
    void professorApagarInexistenteRetorna404() throws Exception {
        mvc.perform(delete("/professores/99999").header("Authorization", "Bearer " + tokenAdm))
            .andExpect(status().isNotFound());
    }

    @Test
    void materialBuscarInexistenteRetorna404() throws Exception {
        mvc.perform(get("/materiais/99999").header("Authorization", "Bearer " + tokenSol))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.erro").exists());
    }

    @Test
    void materialBuscarExistenteRetorna200() throws Exception {
        Long id = criarMaterial("M1", "Eletronico", 2);
        mvc.perform(get("/materiais/" + id).header("Authorization", "Bearer " + tokenSol))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void materialAtualizarExistenteRetorna200() throws Exception {
        Long id = criarMaterial("M2", "Eletronico", 2);
        mvc.perform(put("/materiais/" + id)
                .header("Authorization", "Bearer " + tokenAdm)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"M2 novo\",\"tipo\":\"Rede\",\"quantidade\":5}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.tipo").value("Rede"))
            .andExpect(jsonPath("$.quantidade").value(5));
    }

    @Test
    void materialAtualizarInexistenteRetorna404() throws Exception {
        mvc.perform(put("/materiais/99999")
                .header("Authorization", "Bearer " + tokenAdm)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"X\",\"tipo\":\"Y\",\"quantidade\":1}"))
            .andExpect(status().isNotFound());
    }

    @Test
    void materialApagarExistenteRetorna204() throws Exception {
        Long id = criarMaterial("M3", "X", 1);
        mvc.perform(delete("/materiais/" + id).header("Authorization", "Bearer " + tokenAdm))
            .andExpect(status().isNoContent());
    }

    @Test
    void materialApagarInexistenteRetorna404() throws Exception {
        mvc.perform(delete("/materiais/99999").header("Authorization", "Bearer " + tokenAdm))
            .andExpect(status().isNotFound());
    }

    @Test
    void professorComAtivoNuloDefaultTrue() throws Exception {
        Long id = criarProfessor("P4", "p4@x.com", "Java");
        mvc.perform(get("/professores/" + id).header("Authorization", "Bearer " + tokenSol))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.ativo").value(true));
    }

    private Long criarProfessor(String nome, String email, String comp) throws Exception {
        MvcResult r = mvc.perform(post("/professores")
                .header("Authorization", "Bearer " + tokenAdm)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"" + nome + "\",\"email\":\"" + email + "\",\"competencia\":\"" + comp + "\"}"))
            .andExpect(status().isCreated()).andReturn();
        return extrairId(r);
    }

    private Long criarMaterial(String nome, String tipo, int qtd) throws Exception {
        MvcResult r = mvc.perform(post("/materiais")
                .header("Authorization", "Bearer " + tokenAdm)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"" + nome + "\",\"tipo\":\"" + tipo + "\",\"quantidade\":" + qtd + "}"))
            .andExpect(status().isCreated()).andReturn();
        return extrairId(r);
    }

    private Long extrairId(MvcResult r) throws Exception {
        Matcher m = Pattern.compile("\"id\":(\\d+)").matcher(r.getResponse().getContentAsString());
        if (!m.find()) throw new IllegalStateException("resposta sem id");
        return Long.parseLong(m.group(1));
    }
}
