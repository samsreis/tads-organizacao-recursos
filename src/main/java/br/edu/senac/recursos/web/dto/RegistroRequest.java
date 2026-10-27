package br.edu.senac.recursos.web.dto;

import br.edu.senac.recursos.domain.Perfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistroRequest(
        @NotBlank @Email String email,
        @NotBlank String senha,
        @NotBlank String nome,
        @NotNull Perfil perfil
) {}
