package br.com.ControleFinanceiro.API.dto.request;

import br.com.ControleFinanceiro.API.entity.emuns.TomIA;
import jakarta.validation.constraints.NotNull;

public record AtualizarTomIaRequest(
        @NotNull(message = "O tom da IA é obrigatório")
        TomIA tomIA
) {}