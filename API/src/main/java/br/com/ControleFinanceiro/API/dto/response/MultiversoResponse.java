package br.com.ControleFinanceiro.API.dto.response;

import java.math.BigDecimal;

public record MultiversoResponse(
        BigDecimal acumuladoEmUmAno,
        BigDecimal acumuladoEmCincoAnos,
        BigDecimal acumuladoEmDezAnos
) {}