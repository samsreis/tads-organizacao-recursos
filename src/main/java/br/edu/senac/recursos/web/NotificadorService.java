package br.edu.senac.recursos.web;

import br.edu.senac.recursos.config.NotificadorProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.Map;

@Service
public class NotificadorService {

    private static final Logger log = LoggerFactory.getLogger(NotificadorService.class);

    private final RestClient client;

    public NotificadorService(NotificadorProperties props) {
        SimpleClientHttpRequestFactory f = new SimpleClientHttpRequestFactory();
        f.setConnectTimeout(Duration.ofMillis(props.getTimeoutMs()));
        f.setReadTimeout(Duration.ofMillis(props.getTimeoutMs()));
        this.client = RestClient.builder()
                .baseUrl(props.getBaseUrl())
                .requestFactory(f)
                .build();
    }

    public void notificar(Long reservaId, String status, String email) {
        try {
            client.post()
                .uri("/notificar")
                .body(Map.of("reservaId", reservaId, "status", status, "usuario", email))
                .retrieve()
                .toBodilessEntity();
        } catch (Exception e) {
            log.warn("Falha ao notificar reserva {} status {}: {}", reservaId, status, e.getMessage());
        }
    }
}
