package br.com.ControleFinanceiro.API.dto.response;

import br.com.ControleFinanceiro.API.entity.emuns.TipoCategoria;

import java.math.BigDecimal;

public record OrcamentoResponse(
        Long id,
        Integer mes,
        Integer ano,
        BigDecimal valorPlanejado,
        Long categoriaId,
        String nomeCategoria,
        String tipoCategoria,
        BigDecimal valorGasto
) {}