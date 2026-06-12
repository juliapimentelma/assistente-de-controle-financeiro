package br.com.ControleFinanceiro.API.service;

import br.com.ControleFinanceiro.API.dto.response.MultiversoResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Service
public class MultiversoService {

    private static final BigDecimal TAXA_JUROS_MENSAL = new BigDecimal("0.008");

    public MultiversoResponse simular(BigDecimal valorMensalCortado) {

        if (valorMensalCortado == null || valorMensalCortado.compareTo(BigDecimal.ZERO) <= 0) {
            return new MultiversoResponse(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        }

        BigDecimal acumulado1Ano = projetarValorFuturo(valorMensalCortado, 12);

        BigDecimal acumulado5Anos = projetarValorFuturo(valorMensalCortado, 60);

        // Cenário 3: 10 Anos (120 meses)
        BigDecimal acumulado10Anos = projetarValorFuturo(valorMensalCortado, 120);

        return new MultiversoResponse(acumulado1Ano, acumulado5Anos, acumulado10Anos);
    }

    private BigDecimal projetarValorFuturo(BigDecimal aporteMensal, int meses) {

        BigDecimal umMaisI = BigDecimal.ONE.add(TAXA_JUROS_MENSAL);

        BigDecimal potencia = umMaisI.pow(meses, MathContext.DECIMAL64);

        BigDecimal numerador = potencia.subtract(BigDecimal.ONE);

        BigDecimal fatorMultiplicador = numerador.divide(TAXA_JUROS_MENSAL, MathContext.DECIMAL64);

        return aporteMensal.multiply(fatorMultiplicador).setScale(2, RoundingMode.HALF_UP);
    }
}