package br.com.ControleFinanceiro.API.dto.request;

import jakarta.validation.constraints.NotNull;

public record AtualizarModoEscuroRequest(
        @NotNull(message = "A preferência de modo escuro é obrigatória")
        Boolean modoEscuro
) {}
