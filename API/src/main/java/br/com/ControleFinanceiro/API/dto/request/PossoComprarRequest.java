package br.com.ControleFinanceiro.API.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record PossoComprarRequest(
        @NotBlank(message = "Informe o nome do item que deseja comprar")
        String item,

        @NotNull(message = "Informe o valor do item")
        @Positive(message = "O valor deve ser positivo")
        BigDecimal valor
) {}