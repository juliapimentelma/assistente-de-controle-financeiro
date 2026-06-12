package br.com.ControleFinanceiro.API.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AporteRequest(
        @NotNull(message = "O valor do aporte é obrigatório")
        @Positive(message = "O valor do aporte deve ser maior que zero")
        BigDecimal valor
) {}
