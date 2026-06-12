package br.com.ControleFinanceiro.API.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record OrcamentoRequest(
        @NotNull(message = "O mês é obrigatório")
        @Min(value = 3) @Max(value = 12)
        Integer mes,

        @NotNull(message = "O ano é obrigatório")
        Integer ano,

        @NotNull(message = "O valor planejado é obrigatório")
        @PositiveOrZero(message = "O valor planejado não pode ser negativo")
        BigDecimal valorPlanejado,

        @NotNull(message = "O ID da categoria é obrigatório")
        Long categoriaId
) {}