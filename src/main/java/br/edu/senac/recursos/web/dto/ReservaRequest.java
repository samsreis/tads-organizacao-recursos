package br.edu.senac.recursos.web.dto;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public record ReservaRequest(
        @NotNull Long salaId,
        @NotNull OffsetDateTime inicio,
        @NotNull OffsetDateTime fim
) {}
