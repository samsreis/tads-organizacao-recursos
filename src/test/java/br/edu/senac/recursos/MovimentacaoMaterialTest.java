package br.edu.senac.recursos;

import br.edu.senac.recursos.domain.Material;
import br.edu.senac.recursos.repository.MaterialRepository;
import br.edu.senac.recursos.repository.MovimentacaoMaterialRepository;
import br.edu.senac.recursos.suporte.BaseIntegracaoTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MovimentacaoMaterialTest extends BaseIntegracaoTest {

    @Autowired MaterialRepository materiais;
    @Autowired MovimentacaoMaterialRepository movs;
    Long materialId;

    @BeforeEach
    void criarMaterial() {
        movs.deleteAll();
        materiais.deleteAll();
        Material m = new Material();
        m.setNome("Projetor");
        m.setTipo("Equipamento");
        m.setQuantidade(5);
        m.setEmManutencao(false);
        materiais.save(m);
        materialId = m.getId();
    }

    @Test
    void responsavelRetiraTresDeCinco() throws Exception {
        mvc.perform(post("/materiais/" + materialId + "/retiradas")
                .header("Authorization", "Bearer " + tokenRes)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"quantidade\":3}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.tipo").value("RETIRADA"))
            .andExpect(jsonPath("$.quantidade").value(3));
        mvc.perform(get("/materiais/" + materialId + "/saldo").header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.disponivel").value(2))
            .andExpect(jsonPath("$.emPosse").value(3));
    }

    @Test
    void retiradaMaiorQueDisponivel409() throws Exception {
        mvc.perform(post("/materiais/" + materialId + "/retiradas")
                .header("Authorization", "Bearer " + tokenRes)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"quantidade\":6}"))
            .andExpect(status().isConflict());
    }

    @Test
    void devolucaoAposRetirada() throws Exception {
        mvc.perform(post("/materiais/" + materialId + "/retiradas")
                .header("Authorization", "Bearer " + tokenRes)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"quantidade\":3}"))
            .andExpect(status().isCreated());
        mvc.perform(post("/materiais/" + materialId + "/devolucoes")
                .header("Authorization", "Bearer " + tokenRes)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"quantidade\":2}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.tipo").value("DEVOLUCAO"));
        mvc.perform(get("/materiais/" + materialId + "/saldo").header("Authorization", "Bearer " + tokenRes))
            .andExpect(jsonPath("$.disponivel").value(4))
            .andExpect(jsonPath("$.emPosse").value(1));
    }

    @Test
    void devolucaoMaiorQueEmPosse409() throws Exception {
        mvc.perform(post("/materiais/" + materialId + "/retiradas")
                .header("Authorization", "Bearer " + tokenRes)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"quantidade\":3}"))
            .andExpect(status().isCreated());
        mvc.perform(post("/materiais/" + materialId + "/devolucoes")
                .header("Authorization", "Bearer " + tokenRes)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"quantidade\":4}"))
            .andExpect(status().isConflict());
    }

    @Test
    void solicitanteNaoPodeRetirar403() throws Exception {
        mvc.perform(post("/materiais/" + materialId + "/retiradas")
                .header("Authorization", "Bearer " + tokenSol)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"quantidade\":1}"))
            .andExpect(status().isForbidden());
    }

    @Test
    void anonimoNaoPodeRetirar401() throws Exception {
        mvc.perform(post("/materiais/" + materialId + "/retiradas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"quantidade\":1}"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void materialInexistente404() throws Exception {
        mvc.perform(post("/materiais/99999/retiradas")
                .header("Authorization", "Bearer " + tokenRes)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"quantidade\":1}"))
            .andExpect(status().isNotFound());
    }

    @Test
    void materialEmManutencao409() throws Exception {
        Material m = new Material();
        m.setNome("Cabos");
        m.setTipo("Equipamento");
        m.setQuantidade(3);
        m.setEmManutencao(true);
        materiais.save(m);
        mvc.perform(post("/materiais/" + m.getId() + "/retiradas")
                .header("Authorization", "Bearer " + tokenRes)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"quantidade\":1}"))
            .andExpect(status().isConflict());
    }

    @Test
    void quantidadeZero400() throws Exception {
        mvc.perform(post("/materiais/" + materialId + "/retiradas")
                .header("Authorization", "Bearer " + tokenRes)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"quantidade\":0}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void historicoListaMovimentacoes() throws Exception {
        mvc.perform(post("/materiais/" + materialId + "/retiradas")
                .header("Authorization", "Bearer " + tokenRes)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"quantidade\":2}"))
            .andExpect(status().isCreated());
        mvc.perform(post("/materiais/" + materialId + "/devolucoes")
                .header("Authorization", "Bearer " + tokenRes)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"quantidade\":1}"))
            .andExpect(status().isCreated());
        mvc.perform(get("/materiais/" + materialId + "/movimentacoes").header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].tipo").value("RETIRADA"))
            .andExpect(jsonPath("$[1].tipo").value("DEVOLUCAO"));
    }
}
