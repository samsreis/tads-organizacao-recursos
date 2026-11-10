package br.edu.senac.recursos;

import br.edu.senac.recursos.domain.Professor;
import br.edu.senac.recursos.domain.Sala;
import br.edu.senac.recursos.repository.ProfessorRepository;
import br.edu.senac.recursos.suporte.BaseIntegracaoTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.UUID;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AutorizacaoPorPerfilTest extends BaseIntegracaoTest {

    @Autowired
    private ProfessorRepository professores;

    static Stream<Arguments> matriz() {
        return Stream.of(
            Arguments.of("GET", "/salas", "ANONIMO", 401),
            Arguments.of("GET", "/salas", "SOLICITANTE", 200),
            Arguments.of("GET", "/salas", "RESPONSAVEL", 200),
            Arguments.of("GET", "/salas", "ADMINISTRADOR", 200),
            Arguments.of("POST", "/salas", "ANONIMO", 401),
            Arguments.of("POST", "/salas", "SOLICITANTE", 403),
            Arguments.of("POST", "/salas", "RESPONSAVEL", 403),
            Arguments.of("POST", "/salas", "ADMINISTRADOR", 201),
            Arguments.of("POST", "/reservas", "ANONIMO", 401),
            Arguments.of("POST", "/reservas", "SOLICITANTE", 201),
            Arguments.of("POST", "/reservas", "RESPONSAVEL", 201),
            Arguments.of("POST", "/reservas", "ADMINISTRADOR", 201),
            Arguments.of("GET", "/reservas/minhas", "ANONIMO", 401),
            Arguments.of("GET", "/reservas/minhas", "SOLICITANTE", 200),
            Arguments.of("GET", "/reservas", "ANONIMO", 401),
            Arguments.of("GET", "/reservas", "SOLICITANTE", 403),
            Arguments.of("GET", "/reservas", "RESPONSAVEL", 200),
            Arguments.of("GET", "/reservas", "ADMINISTRADOR", 200)
        );
    }

    @ParameterizedTest(name = "{0} {1} como {2} => {3}")
    @MethodSource("matriz")
    void matrizDeAutorizacao(String metodo, String path, String perfil, int esperado) throws Exception {
        MockHttpServletRequestBuilder req = switch (metodo) {
            case "GET" -> get(path);
            case "POST" -> post(path);
            case "PUT" -> put(path);
            case "DELETE" -> delete(path);
            default -> throw new IllegalArgumentException(metodo);
        };
        switch (perfil) {
            case "SOLICITANTE" -> req = req.header("Authorization", "Bearer " + tokenSol);
            case "RESPONSAVEL" -> req = req.header("Authorization", "Bearer " + tokenRes);
            case "ADMINISTRADOR" -> req = req.header("Authorization", "Bearer " + tokenAdm);
        }
        if (metodo.equals("POST") && path.equals("/salas")) {
            req = req.contentType(MediaType.APPLICATION_JSON)
                     .content("{\"nome\":\"Nova\",\"capacidade\":10,\"localizacao\":\"X\"}");
        } else if (metodo.equals("POST") && path.equals("/reservas")) {
            req = req.contentType(MediaType.APPLICATION_JSON)
                     .content("{\"salaId\":" + salaId + ",\"inicio\":\"2026-12-01T10:00:00-03:00\",\"fim\":\"2026-12-01T11:00:00-03:00\"}");
        }
        mvc.perform(req).andExpect(status().is(esperado));
    }

    @Test
    void solicitanteConsultaSalaPorId200() throws Exception {
        mvc.perform(get("/salas/" + salaId)
                .header("Authorization", "Bearer " + tokenSol))
            .andExpect(status().isOk());
    }

    @Test
    void solicitanteConsultaProfessorPorId200() throws Exception {
        Professor professor = new Professor();
        professor.setNome("Professor de teste");
        professor.setEmail(UUID.randomUUID() + "@teste.com");
        professor.setCompetencia("Qualidade de Software");
        Long professorId = professores.save(professor).getId();

        mvc.perform(get("/professores/" + professorId)
                .header("Authorization", "Bearer " + tokenSol))
            .andExpect(status().isOk());
    }
}
