package br.com.ControleFinanceiro.API.dto;

import java.math.BigDecimal;

public record CategoriaSomaDTO(
        String nomeCategoria,
        BigDecimal somaValor
) {}