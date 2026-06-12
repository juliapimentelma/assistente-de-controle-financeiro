package br.com.ControleFinanceiro.API.dto.response;

import br.com.ControleFinanceiro.API.entity.emuns.StatusTransacao;
import br.com.ControleFinanceiro.API.entity.emuns.TipoCategoria;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoResponse(
        Long id,
        String descricao,
        BigDecimal valor,
        LocalDate dataVencimento,
        Integer mesCompetencia,
        Integer anoCompetencia,
        StatusTransacao status,
        String nomeSubcategoria,
        String nomeCategoriaMaior,
        TipoCategoria tipo
) {}
