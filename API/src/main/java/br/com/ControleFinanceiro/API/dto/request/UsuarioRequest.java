package br.com.ControleFinanceiro.API.dto.request;

import br.com.ControleFinanceiro.API.validator.SenhaForte;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email
        String email,

        @NotBlank(message = "A senha é obrigatória")
        @SenhaForte
        String senha
) {}