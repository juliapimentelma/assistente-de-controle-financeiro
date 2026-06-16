package br.com.ControleFinanceiro.API.dto;

public record TransacaoResumo(
        String descricao,
        String tipo,
        String categoria,
        String data,
        String status,
        java.math.BigDecimal valor
) {}