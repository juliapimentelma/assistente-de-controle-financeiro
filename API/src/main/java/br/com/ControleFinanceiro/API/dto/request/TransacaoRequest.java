package br.com.ControleFinanceiro.API.dto.request;

import br.com.ControleFinanceiro.API.entity.emuns.FrequenciaParcela;
import br.com.ControleFinanceiro.API.entity.emuns.StatusTransacao;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoRequest(
        @NotBlank(message = "A descrição é obrigatória")
        @Size(max = 255)
        String descricao,

        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor deve ser maior que zero")
        BigDecimal valor,

        @NotNull(message = "A data de vencimento é obrigatória")
        LocalDate dataVencimento,

        @NotNull(message = "O status da transação é obrigatório")
        StatusTransacao status,

        @NotNull(message = "O ID da categoria é obrigatório")
        Long categoriaId,

        String nomeSubcategoria,

        @Min(value = 1, message = "A quantidade de parcelas mínima é 1")
        Integer qtdParcelas,

        FrequenciaParcela frequencia
) {}