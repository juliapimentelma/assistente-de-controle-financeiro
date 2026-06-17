package br.com.ControleFinanceiro.API.dto;

import java.math.BigDecimal;

public record CategoriaDespesaDTO(
        String nome,
        Double porcentagem
) {}