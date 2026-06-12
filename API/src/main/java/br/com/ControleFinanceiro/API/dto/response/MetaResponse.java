package br.com.ControleFinanceiro.API.dto.response;

import br.com.ControleFinanceiro.API.entity.emuns.StatusMeta;

import java.math.BigDecimal;

public record MetaResponse(
        Long id,
        String titulo,
        BigDecimal valorAlvo,
        BigDecimal valorAtual,
        StatusMeta status
) {}