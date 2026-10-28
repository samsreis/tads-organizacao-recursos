package br.edu.senac.recursos;

import br.edu.senac.recursos.suporte.BaseIntegracaoTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ValidacaoEntradaTest extends BaseIntegracaoTest {

    static Stream<Arguments> casos() {
        return Stream.of(
            Arguments.of("termino igual ao inicio",
                "{\"salaId\":__SALA__,\"inicio\":\"2026-12-01T10:00:00-03:00\",\"fim\":\"2026-12-01T10:00:00-03:00\"}", "/reservas", "SOL"),
            Arguments.of("termino antes do inicio",
                "{\"salaId\":__SALA__,\"inicio\":\"2026-12-01T11:00:00-03:00\",\"fim\":\"2026-12-01T10:00:00-03:00\"}", "/reservas", "SOL"),
            Arguments.of("salaId ausente",
                "{\"inicio\":\"2026-12-01T10:00:00-03:00\",\"fim\":\"2026-12-01T11:00:00-03:00\"}", "/reservas", "SOL"),
            Arguments.of("inicio ausente",
                "{\"salaId\":__SALA__,\"fim\":\"2026-12-01T11:00:00-03:00\"}", "/reservas", "SOL"),
            Arguments.of("capacidade negativa em sala",
                "{\"nome\":\"X\",\"capacidade\":-1,\"localizacao\":\"Y\"}", "/salas", "ADM"),
            Arguments.of("corpo malformado em reserva",
                "{not json", "/reservas", "SOL")
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("casos")
    void validacao(String caso, String corpo, String path, String quem) throws Exception {
        String body = corpo.replace("__SALA__", String.valueOf(salaId));
        String token = switch (quem) {
            case "SOL" -> tokenSol;
            case "ADM" -> tokenAdm;
            default -> throw new IllegalArgumentException(quem);
        };
        mvc.perform(post(path)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.erro").exists())
            .andExpect(content().string(not(containsString("java."))))
            .andExpect(content().string(not(containsString("org.springframework"))))
            .andExpect(content().string(not(containsString("Exception"))));
    }

    @Test
    void registroSemEmailRetorna400ComErro() throws Exception {
        mvc.perform(post("/auth/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"senha\":\"x\",\"nome\":\"X\",\"perfil\":\"SOLICITANTE\"}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.erro").exists());
    }
}
