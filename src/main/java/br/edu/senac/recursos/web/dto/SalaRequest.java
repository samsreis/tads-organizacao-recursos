package br.edu.senac.recursos.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SalaRequest(
        @NotBlank String nome,
        @NotNull @Min(1) Integer capacidade,
        @NotBlank String localizacao,
        Boolean emManutencao
) {}
