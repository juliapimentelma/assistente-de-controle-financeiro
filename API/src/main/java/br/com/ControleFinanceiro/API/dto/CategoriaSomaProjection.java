package br.com.ControleFinanceiro.API.dto;

import java.math.BigDecimal;

public interface CategoriaSomaProjection {
    String getNome();
    BigDecimal getTotal();
}
