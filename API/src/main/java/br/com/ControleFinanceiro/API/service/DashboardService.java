package br.com.ControleFinanceiro.API.service;

import br.com.ControleFinanceiro.API.dto.*;
import br.com.ControleFinanceiro.API.dto.response.DashboardResponse;
import br.com.ControleFinanceiro.API.entity.Transacao;
import br.com.ControleFinanceiro.API.entity.Usuario;
import br.com.ControleFinanceiro.API.repository.MetaProjetoRepository;
import br.com.ControleFinanceiro.API.repository.OrcamentoRepository;
import br.com.ControleFinanceiro.API.repository.TransacaoRepository;
import br.com.ControleFinanceiro.API.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import br.com.ControleFinanceiro.API.entity.emuns.StatusMeta;
import br.com.ControleFinanceiro.API.entity.MetaProjeto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final UsuarioRepository usuarioRepository;
    private final TransacaoRepository transacaoRepository;
    private final OrcamentoRepository orcamentoRepository;
    private final MetaProjetoRepository metaProjetoRepository;


    public DashboardService(UsuarioRepository usuarioRepository,
                            TransacaoRepository transacaoRepository,
                            OrcamentoRepository orcamentoRepository,
                            MetaProjetoRepository metaProjetoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.transacaoRepository = transacaoRepository;
        this.orcamentoRepository = orcamentoRepository;
        this.metaProjetoRepository = metaProjetoRepository;
    }

    public DashboardResponse montarResumo(String tokenSubject) {
        Long userId = Long.parseLong(tokenSubject);
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado na base de dados para o ID: " + userId));

        int mesAtual = LocalDate.now().getMonthValue();
        int anoAtual = LocalDate.now().getYear();

        int progressoMeta = 0;
        Optional<MetaProjeto> metaAtiva = metaProjetoRepository.findFirstByUsuarioIdAndStatus(userId, StatusMeta.EM_ANDAMENTO);

        if (metaAtiva.isPresent()) {
            BigDecimal alvo = metaAtiva.get().getValorAlvo();
            BigDecimal atual = metaAtiva.get().getValorAtual();
            if (alvo.compareTo(BigDecimal.ZERO) > 0) {
                progressoMeta = atual.multiply(new BigDecimal("100"))
                        .divide(alvo, 0, RoundingMode.HALF_UP).intValue();
            }
        }

        BigDecimal orcamentoTotal = orcamentoRepository.somarOrcamentoDoMes(userId, mesAtual, anoAtual);
        BigDecimal despesasTotal = transacaoRepository.somarDespesasDoMes(userId, mesAtual, anoAtual);
        BigDecimal valorDisponivelMes = orcamentoTotal.subtract(despesasTotal);
        if (valorDisponivelMes.compareTo(BigDecimal.ZERO) < 0) {
            valorDisponivelMes = BigDecimal.ZERO;
        }

        List<CategoriaSomaProjection> despesasAgrupadas = transacaoRepository.somarDespesasAgrupadasPorCategoria(userId, mesAtual, anoAtual);

        List<CategoriaResumo> categoriasDespesa = despesasAgrupadas.stream().map(proj -> {
            int porcentagem = proj.getTotal().multiply(new BigDecimal("100"))
                    .divide(despesasTotal, 0, RoundingMode.HALF_UP).intValue();

            return new CategoriaResumo(proj.getNome(), porcentagem);
        }).collect(Collectors.toList());

        List<Transacao> ultimasDoBanco = transacaoRepository.findTop5ByUsuarioIdOrderByDataCriacaoDesc(userId);
        List<TransacaoResumo> ultimasTransacoes = ultimasDoBanco.stream()
                .map(t -> new TransacaoResumo(
                        t.getDescricao(),
                        t.getCategoria().getTipo().name(),
                        t.getCategoria().getNome(),
                        t.getDataCriacao().toString(),
                        t.getStatus().name(),
                        t.getValor()
                )).collect(Collectors.toList());

        return new DashboardResponse(
                usuario.getSaldoAtual(),
                usuario.getScoreFinanceiro(),
                progressoMeta,
                valorDisponivelMes,
                categoriasDespesa,
                ultimasTransacoes
        );
    }

    public List<CategoriaDespesaDTO> calcularCategoriasDespesa(Long usuarioId, int mes, int ano) {

        List<CategoriaSomaDTO> somas = transacaoRepository.somarDespesasPorCategoria(usuarioId, mes, ano);

        if (somas.isEmpty()) {
            return Collections.emptyList();
        }

        BigDecimal totalGasto = somas.stream()
                .map(CategoriaSomaDTO::somaValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return somas.stream()
                .map(soma -> {
                    BigDecimal porcentagem = soma.somaValor()
                            .divide(totalGasto, 4, RoundingMode.HALF_UP)
                            .multiply(new BigDecimal("100"))
                            .setScale(1, RoundingMode.HALF_UP);

                    return new CategoriaDespesaDTO(
                            soma.nomeCategoria(),
                            porcentagem.doubleValue()
                    );
                })
                .sorted(Comparator.comparing(CategoriaDespesaDTO::porcentagem).reversed())
                .collect(Collectors.toList());
    }
}