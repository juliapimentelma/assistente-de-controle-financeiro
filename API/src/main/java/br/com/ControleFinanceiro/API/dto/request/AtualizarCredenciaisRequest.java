package br.com.ControleFinanceiro.API.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AtualizarCredenciaisRequest(
        @NotBlank(message = "O e-mail atual é obrigatório para esta operação.")
        String emailAtual,

        @NotBlank(message = "A senha atual é obrigatória para esta operação.")
        String senhaAtual,

        String novoEmail,
        String novaSenha
) {}