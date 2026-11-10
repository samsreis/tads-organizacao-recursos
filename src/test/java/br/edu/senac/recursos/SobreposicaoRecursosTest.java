package br.edu.senac.recursos;

import br.edu.senac.recursos.suporte.BaseIntegracaoTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SobreposicaoRecursosTest extends BaseIntegracaoTest {

    @Test
    void professorNaoPodeTerDuasReservasSobrepostas() throws Exception {
        Long profId = criarProfessor("ProfA", "profa@x.com", true);
        String inicio = "2026-12-03T10:00:00-03:00";
        String fim = "2026-12-03T11:00:00-03:00";
        Long outraSala = criarSala("Sala Conflito Professor");
        reservarComProfessor(salaId, profId, inicio, fim).andExpect(status().isCreated());
        reservarComProfessor(outraSala, profId, inicio, fim).andExpect(status().isConflict());
    }

    @Test
    void professorPodeTerReservasEmHorariosDistintos() throws Exception {
        Long profId = criarProfessor("ProfB", "profb@x.com", true);
        reservarComProfessor(profId, "2026-12-03T10:00:00-03:00", "2026-12-03T11:00:00-03:00").andExpect(status().isCreated());
        reservarComProfessor(profId, "2026-12-03T11:00:00-03:00", "2026-12-03T12:00:00-03:00").andExpect(status().isCreated());
    }

    @Test
    void materialNaoPodeTerDuasReservasSobrepostas() throws Exception {
        Long matId = criarMaterial("ProjetorX", false);
        String inicio = "2026-12-03T10:00:00-03:00";
        String fim = "2026-12-03T11:00:00-03:00";
        Long outraSala = criarSala("Sala Conflito Material");
        reservarComMaterial(salaId, matId, inicio, fim).andExpect(status().isCreated());
        reservarComMaterial(outraSala, matId, inicio, fim).andExpect(status().isConflict());
    }

    @Test
    void professorInativoNaoPodeSerReservado() throws Exception {
        Long profId = criarProfessor("ProfC", "profc@x.com", false);
        reservarComProfessor(profId, "2026-12-03T10:00:00-03:00", "2026-12-03T11:00:00-03:00")
            .andExpect(status().isConflict());
    }

    @Test
    void materialEmManutencaoNaoPodeSerReservado() throws Exception {
        Long matId = criarMaterial("MatQuebrado", true);
        reservarComMaterial(matId, "2026-12-03T10:00:00-03:00", "2026-12-03T11:00:00-03:00")
            .andExpect(status().isConflict());
    }

    @Test
    void professorInexistenteRetorna404() throws Exception {
        reservarComProfessor(99999L, "2026-12-03T10:00:00-03:00", "2026-12-03T11:00:00-03:00")
            .andExpect(status().isNotFound());
    }

    @Test
    void materialInexistenteRetorna404() throws Exception {
        reservarComMaterial(99999L, "2026-12-03T10:00:00-03:00", "2026-12-03T11:00:00-03:00")
            .andExpect(status().isNotFound());
    }

    @Test
    void reservaComSalaProfessorEMaterialAoMesmoTempo() throws Exception {
        Long profId = criarProfessor("ProfD", "profd@x.com", true);
        Long matId = criarMaterial("MatBom", false);
        mvc.perform(post("/reservas")
                .header("Authorization", "Bearer " + tokenSol)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"salaId\":" + salaId + ",\"professorId\":" + profId
                        + ",\"materialId\":" + matId
                        + ",\"inicio\":\"2026-12-04T10:00:00-03:00\",\"fim\":\"2026-12-04T11:00:00-03:00\"}"))
            .andExpect(status().isCreated());
    }

    private org.springframework.test.web.servlet.ResultActions reservarComProfessor(Long profId, String ini, String fim) throws Exception {
        return reservarComProfessor(salaId, profId, ini, fim);
    }

    private org.springframework.test.web.servlet.ResultActions reservarComProfessor(Long sala, Long profId, String ini, String fim) throws Exception {
        return mvc.perform(post("/reservas")
                .header("Authorization", "Bearer " + tokenSol)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"salaId\":" + sala + ",\"professorId\":" + profId
                        + ",\"inicio\":\"" + ini + "\",\"fim\":\"" + fim + "\"}"));
    }

    private org.springframework.test.web.servlet.ResultActions reservarComMaterial(Long matId, String ini, String fim) throws Exception {
        return reservarComMaterial(salaId, matId, ini, fim);
    }

    private org.springframework.test.web.servlet.ResultActions reservarComMaterial(Long sala, Long matId, String ini, String fim) throws Exception {
        return mvc.perform(post("/reservas")
                .header("Authorization", "Bearer " + tokenSol)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"salaId\":" + sala + ",\"materialId\":" + matId
                        + ",\"inicio\":\"" + ini + "\",\"fim\":\"" + fim + "\"}"));
    }

    private Long criarProfessor(String nome, String email, boolean ativo) throws Exception {
        MvcResult r = mvc.perform(post("/professores")
                .header("Authorization", "Bearer " + tokenAdm)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"" + nome + "\",\"email\":\"" + email
                        + "\",\"competencia\":\"X\",\"ativo\":" + ativo + "}"))
            .andExpect(status().isCreated()).andReturn();
        return extrairId(r);
    }

    private Long criarMaterial(String nome, boolean emManutencao) throws Exception {
        MvcResult r = mvc.perform(post("/materiais")
                .header("Authorization", "Bearer " + tokenAdm)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"" + nome + "\",\"tipo\":\"X\",\"quantidade\":1,\"emManutencao\":" + emManutencao + "}"))
            .andExpect(status().isCreated()).andReturn();
        return extrairId(r);
    }

    private Long criarSala(String nome) throws Exception {
        MvcResult r = mvc.perform(post("/salas")
                .header("Authorization", "Bearer " + tokenAdm)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"" + nome + "\",\"capacidade\":30,\"localizacao\":\"Bloco X\"}"))
            .andExpect(status().isCreated()).andReturn();
        return extrairId(r);
    }

    private Long extrairId(MvcResult r) throws Exception {
        Matcher m = Pattern.compile("\"id\":(\\d+)").matcher(r.getResponse().getContentAsString());
        if (!m.find()) throw new IllegalStateException("resposta sem id");
        return Long.parseLong(m.group(1));
    }
}
