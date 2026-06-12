package br.com.ControleFinanceiro.API.dto.response;

import java.math.BigDecimal;

public record AnaliseCompraResponse(
        BigDecimal saldoAtual,
        BigDecimal saldoAposCompra,
        boolean vaiFicarNegativado
) {}