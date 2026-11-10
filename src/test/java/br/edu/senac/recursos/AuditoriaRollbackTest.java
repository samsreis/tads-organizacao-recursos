package br.edu.senac.recursos;

import br.edu.senac.recursos.domain.AuditoriaReserva;
import br.edu.senac.recursos.repository.AuditoriaReservaRepository;
import br.edu.senac.recursos.suporte.BaseIntegracaoTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class AuditoriaRollbackTest extends BaseIntegracaoTest {

    @MockitoSpyBean
    AuditoriaReservaRepository auditoriaRepo;

    @Test
    void falhaNaAuditoriaDesfazAReserva() {
        long reservasAntes = reservas.count();
        long auditoriasAntes = auditoriaRepo.count();

        doThrow(new RuntimeException("falha simulada na gravacao da auditoria"))
            .when(auditoriaRepo).save(any(AuditoriaReserva.class));

        assertThatThrownBy(() -> mvc.perform(post("/reservas")
                .header("Authorization", "Bearer " + tokenSol)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content("{\"salaId\":" + salaId + ",\"inicio\":\"2026-12-26T10:00:00-03:00\",\"fim\":\"2026-12-26T11:00:00-03:00\"}")))
            .hasMessageContaining("falha simulada na gravacao da auditoria");

        assertThat(reservas.count()).isEqualTo(reservasAntes);
        assertThat(auditoriaRepo.count()).isEqualTo(auditoriasAntes);
    }
}
