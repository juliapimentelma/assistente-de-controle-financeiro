package br.com.ControleFinanceiro.API.dto.response;

import br.com.ControleFinanceiro.API.dto.CategoriaResumo;
import br.com.ControleFinanceiro.API.dto.TransacaoResumo;

import java.math.BigDecimal;
import java.util.List;

public record DashboardResponse(
        BigDecimal saldoAtual,
        Integer scoreFinanceiro,
        Integer progressoMeta,
        BigDecimal valorDisponivelMes,
        List<CategoriaResumo> categoriasDespesa,
        List<TransacaoResumo> ultimasTransacoes
) {}