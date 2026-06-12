package br.com.ControleFinanceiro.API.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record MetaRequest(
        @NotBlank(message = "O título da meta é obrigatório")
        String titulo,

        @NotNull(message = "O valor alvo é obrigatório")
        @Positive(message = "O valor alvo deve ser maior que zero")
        BigDecimal valorAlvo
) {}