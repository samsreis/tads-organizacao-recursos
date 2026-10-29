package br.edu.senac.recursos.web.dto;

import br.edu.senac.recursos.domain.Reserva;
import java.time.OffsetDateTime;

public record ReservaResponse(
        Long id,
        Long salaId,
        Long professorId,
        Long materialId,
        String solicitanteEmail,
        OffsetDateTime inicio,
        OffsetDateTime fim,
        String status
) {
    public static ReservaResponse de(Reserva r) {
        return new ReservaResponse(
                r.getId(),
                r.getSala().getId(),
                r.getProfessor() != null ? r.getProfessor().getId() : null,
                r.getMaterial() != null ? r.getMaterial().getId() : null,
                r.getSolicitante().getEmail(),
                r.getInicio(),
                r.getFim(),
                r.getStatus().name());
    }
}
