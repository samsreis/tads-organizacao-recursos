package br.edu.senac.recursos.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MovimentacaoRequest(@NotNull @Min(1) Integer quantidade) {
}
