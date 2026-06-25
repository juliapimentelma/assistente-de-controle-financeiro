package br.com.ControleFinanceiro.API.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtualizarPerfilRequest(
        @NotBlank(message = "O nome não pode estar vazio.")
        String nome,

        @NotNull(message = "O Tom da IA é obrigatório.")
        String tomIA
) {}