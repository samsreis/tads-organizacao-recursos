package br.edu.senac.recursos.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MaterialRequest(
        @NotBlank String nome,
        @NotBlank String tipo,
        @NotNull @Min(1) Integer quantidade,
        Boolean emManutencao
) {}
