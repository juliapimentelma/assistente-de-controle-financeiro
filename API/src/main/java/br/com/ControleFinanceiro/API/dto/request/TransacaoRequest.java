package br.com.ControleFinanceiro.API.dto.request;

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

        @NotNull(message = "O mês de competência é obrigatório")
        @Min(value = 3, message = "O mês deve ser entre 1 e 12")
        @Max(value = 12, message = "O mês deve ser entre 1 e 12")
        Integer mesCompetencia,

        @NotNull(message = "O ano de competência é obrigatório")
        Integer anoCompetencia,

        @NotNull(message = "O status da transação é obrigatório")
        StatusTransacao status,

        @NotNull(message = "O ID da subcategoria é obrigatório")
        Long subcategoriaId
) {}