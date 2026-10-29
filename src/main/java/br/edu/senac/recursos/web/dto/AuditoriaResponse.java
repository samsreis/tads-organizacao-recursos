package br.edu.senac.recursos.web.dto;

import br.edu.senac.recursos.domain.AuditoriaReserva;
import java.time.OffsetDateTime;

public record AuditoriaResponse(
        Long id,
        Long reservaId,
        String usuarioEmail,
        OffsetDateTime instante,
        String estadoAnterior,
        String estadoNovo,
        String motivo
) {
    public static AuditoriaResponse de(AuditoriaReserva a) {
        return new AuditoriaResponse(a.getId(), a.getReservaId(), a.getUsuarioEmail(),
                a.getInstante(), a.getEstadoAnterior(), a.getEstadoNovo(), a.getMotivo());
    }
}
