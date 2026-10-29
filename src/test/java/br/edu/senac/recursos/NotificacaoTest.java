package br.edu.senac.recursos;

import br.edu.senac.recursos.suporte.BaseIntegracaoTest;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NotificacaoTest extends BaseIntegracaoTest {

    static WireMockServer wireMock;

    @BeforeAll
    static void startWireMock() {
        wireMock = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        wireMock.start();
    }

    @AfterAll
    static void stopWireMock() {
        if (wireMock != null) wireMock.stop();
    }

    @DynamicPropertySource
    static void notificadorProps(DynamicPropertyRegistry r) {
        r.add("app.notificador.base-url", () -> "http://localhost:" + wireMock.port());
        r.add("app.notificador.timeout-ms", () -> "800");
    }

    @BeforeEach
    void limparStubs() {
        wireMock.resetAll();
    }

    @Test
    void aprovarEnviaNotificacaoParaProvedor() throws Exception {
        wireMock.stubFor(com.github.tomakehurst.wiremock.client.WireMock.post(urlEqualTo("/notificar"))
            .willReturn(aResponse().withStatus(200)));
        Long id = reservar("2026-12-10T10:00:00-03:00", "2026-12-10T11:00:00-03:00");
        wireMock.resetRequests();
        mvc.perform(post("/reservas/" + id + "/aprovar").header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isOk());
        assertThat(chamadas()).isEqualTo(1);
    }

    @Test
    void provedorComErro500NaoQuebraTransicao() throws Exception {
        wireMock.stubFor(com.github.tomakehurst.wiremock.client.WireMock.post(urlEqualTo("/notificar"))
            .willReturn(aResponse().withStatus(500)));
        Long id = reservar("2026-12-10T10:00:00-03:00", "2026-12-10T11:00:00-03:00");
        wireMock.resetRequests();
        mvc.perform(post("/reservas/" + id + "/aprovar").header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isOk());
        assertThat(chamadas()).isEqualTo(1);
    }

    @Test
    void provedorLentoNaoQuebraTransicao() throws Exception {
        wireMock.stubFor(com.github.tomakehurst.wiremock.client.WireMock.post(urlEqualTo("/notificar"))
            .willReturn(aResponse().withStatus(200).withFixedDelay(3000)));
        Long id = reservar("2026-12-10T10:00:00-03:00", "2026-12-10T11:00:00-03:00");
        mvc.perform(post("/reservas/" + id + "/aprovar").header("Authorization", "Bearer " + tokenRes))
            .andExpect(status().isOk());
    }

    @Test
    void criacaoDeReservaTambemNotifica() throws Exception {
        wireMock.stubFor(com.github.tomakehurst.wiremock.client.WireMock.post(urlEqualTo("/notificar"))
            .willReturn(aResponse().withStatus(200)));
        reservar("2026-12-10T10:00:00-03:00", "2026-12-10T11:00:00-03:00");
        assertThat(chamadas()).isEqualTo(1);
    }

    private int chamadas() {
        return wireMock.findAll(com.github.tomakehurst.wiremock.client.WireMock
            .postRequestedFor(urlEqualTo("/notificar"))).size();
    }

    private Long reservar(String ini, String fim) throws Exception {
        MvcResult r = mvc.perform(post("/reservas")
                .header("Authorization", "Bearer " + tokenSol)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"salaId\":" + salaId + ",\"inicio\":\"" + ini + "\",\"fim\":\"" + fim + "\"}"))
            .andExpect(status().isCreated()).andReturn();
        Matcher m = Pattern.compile("\"id\":(\\d+)").matcher(r.getResponse().getContentAsString());
        if (!m.find()) throw new IllegalStateException("resposta sem id");
        return Long.parseLong(m.group(1));
    }
}
