package br.edu.senac.recursos.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ProfessorRequest(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank String competencia,
        Boolean ativo
) {}
