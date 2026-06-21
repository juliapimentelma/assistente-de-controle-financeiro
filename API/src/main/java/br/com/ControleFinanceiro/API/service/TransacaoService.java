package br.com.ControleFinanceiro.API.service;

import br.com.ControleFinanceiro.API.dto.request.TransacaoRequest;
import br.com.ControleFinanceiro.API.dto.response.TransacaoResponse;
import br.com.ControleFinanceiro.API.entity.*;
import br.com.ControleFinanceiro.API.entity.emuns.FrequenciaParcela;
import br.com.ControleFinanceiro.API.entity.emuns.StatusTransacao;
import br.com.ControleFinanceiro.API.entity.emuns.TipoCategoria;
import br.com.ControleFinanceiro.API.exception.NegocioException;
import br.com.ControleFinanceiro.API.mapper.TransacaoMapper;
import br.com.ControleFinanceiro.API.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SubcategoriaRepository subcategoriaRepository;
    private final CategoriaRepository categoriaRepository;
    private final OrcamentoRepository orcamentoRepository;
    private final TransacaoMapper transacaoMapper;

    @Transactional
    public List<TransacaoResponse> criar(TransacaoRequest dto) {
        Usuario usuario = getUsuarioLogado();

        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada."));

        if (!categoria.getUsuario().getId().equals(usuario.getId())) {
            throw new NegocioException("Esta categoria não pertence a você.");
        }

        Subcategoria subcategoria = null;

        if (dto.nomeSubcategoria() != null && !dto.nomeSubcategoria().trim().isEmpty()) {
            subcategoria = subcategoriaRepository
                    .findByNomeIgnoreCaseAndCategoriaId(dto.nomeSubcategoria().trim(), categoria.getId())
                    .orElseGet(() -> {
                        Subcategoria nova = new Subcategoria();
                        nova.setNome(dto.nomeSubcategoria().trim());
                        nova.setCategoria(categoria);
                        return subcategoriaRepository.save(nova);
                    });
        }

        int totalParcelas = (dto.qtdParcelas() != null && dto.qtdParcelas() > 0) ? dto.qtdParcelas() : 1;
        FrequenciaParcela frequencia = dto.frequencia() != null ? dto.frequencia() : FrequenciaParcela.MENSAL;

        BigDecimal valorParcela = dto.valor().divide(new BigDecimal(totalParcelas), 2, RoundingMode.HALF_UP);
        LocalDate dataVencimentoBase = dto.dataVencimento();

        String grupoId = java.util.UUID.randomUUID().toString();

        List<Transacao> transacoesSalvas = new ArrayList<>();

        for (int i = 1; i <= totalParcelas; i++) {

            Transacao transacao = Transacao.builder()
                    .descricao(dto.descricao())
                    .valor(valorParcela)
                    .dataCriacao(LocalDate.now())
                    .dataVencimento(dataVencimentoBase)
                    .mesCompetencia(dataVencimentoBase.getMonthValue())
                    .anoCompetencia(dataVencimentoBase.getYear())
                    .status(dto.status())
                    .categoria(categoria)
                    .subcategoria(subcategoria)
                    .usuario(usuario)
                    .parcelaAtual(i)
                    .totalParcelas(totalParcelas)
                    .grupoId(grupoId)
                    .build();

            if (transacao.getStatus() == StatusTransacao.PAGO) {
                processarPagamento(usuario, transacao);
            }

            transacoesSalvas.add(transacaoRepository.save(transacao));

            if (i < totalParcelas) {
                dataVencimentoBase = calcularProximaData(dataVencimentoBase, frequencia);
            }
        }

        usuarioRepository.save(usuario);

        return transacoesSalvas.stream()
                .map(transacaoMapper::toResponseDTO)
                .toList();
    }

    public List<TransacaoResponse> listarPorGrupo(String grupoId) {
        Long usuarioId = getUsuarioLogadoId();

        return transacaoRepository.findByUsuarioIdAndGrupoIdOrderByParcelaAtualAsc(usuarioId, grupoId)
                .stream()
                .map(transacaoMapper::toResponseDTO)
                .toList();
    }

    private LocalDate calcularProximaData(LocalDate dataBase, FrequenciaParcela frequencia) {
        return switch (frequencia) {
            case SEMANAL -> dataBase.plusWeeks(1);
            case QUINZENAL -> dataBase.plusDays(15);
            case MENSAL -> dataBase.plusMonths(1);
        };
    }

    public Page<TransacaoResponse> listarPorCompetencia(Integer mes, Integer ano, Pageable pageable) {
        Long usuarioId = getUsuarioLogadoId();

        return transacaoRepository.findAllByUsuarioIdAndMesCompetenciaAndAnoCompetencia(usuarioId, mes, ano, pageable)
                .map(transacaoMapper::toResponseDTO);
    }

    @Transactional
    public void marcarComoPago(Long id) {
        Transacao transacao = buscarTransacaoDoUsuario(id);

        if (transacao.getStatus() == StatusTransacao.PAGO) {
            throw new NegocioException("Esta transação já está paga.");
        }

        transacao.setStatus(StatusTransacao.PAGO);
        Usuario usuario = transacao.getUsuario();

        processarPagamento(usuario, transacao);

        transacaoRepository.save(transacao);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void deletar(Long id) {
        Transacao transacao = buscarTransacaoDoUsuario(id);
        Usuario usuario = transacao.getUsuario();

        if (transacao.getStatus() == StatusTransacao.PAGO) {
            if (transacao.getSubcategoria().getCategoria().getTipo() == TipoCategoria.RECEITA) {
                usuario.setSaldoAtual(usuario.getSaldoAtual().subtract(transacao.getValor()));
            } else {
                usuario.setSaldoAtual(usuario.getSaldoAtual().add(transacao.getValor()));
            }
            usuarioRepository.save(usuario);
        }

        transacaoRepository.delete(transacao);
    }


    private void processarPagamento(Usuario usuario, Transacao transacao) {
        TipoCategoria tipo = transacao.getSubcategoria().getCategoria().getTipo();

        if (tipo == TipoCategoria.RECEITA) {
            usuario.setSaldoAtual(usuario.getSaldoAtual().add(transacao.getValor()));
        } else {
            usuario.setSaldoAtual(usuario.getSaldoAtual().subtract(transacao.getValor()));
        }

        if (usuario.getSaldoAtual().compareTo(BigDecimal.ZERO) < 0) {
            reduzirScore(usuario, 100);
        }

        if (tipo == TipoCategoria.DESPESA) {
            verificarEstouroOrcamento(usuario, transacao);
        }
    }

    private void verificarEstouroOrcamento(Usuario usuario, Transacao novaTransacao) {
        Long categoriaId = novaTransacao.getSubcategoria().getCategoria().getId();
        Integer mes = novaTransacao.getMesCompetencia();
        Integer ano = novaTransacao.getAnoCompetencia();

        Optional<Orcamento> orcamentoOpt = orcamentoRepository.findByUsuarioIdAndCategoriaIdAndMesAndAno(
                usuario.getId(), categoriaId, mes, ano);

        if (orcamentoOpt.isPresent()) {
            BigDecimal teto = orcamentoOpt.get().getValorPlanejado();
            BigDecimal gastoAtual = transacaoRepository.somarGastosPorCategoriaEMes(usuario.getId(), categoriaId, mes, ano);
            BigDecimal gastoProjetado = gastoAtual.add(novaTransacao.getValor());

            if (gastoAtual.compareTo(teto) <= 0 && gastoProjetado.compareTo(teto) > 0) {
                reduzirScore(usuario, 30);
            }
        }
    }

    private void reduzirScore(Usuario usuario, int pontos) {
        int novoScore = usuario.getScoreFinanceiro() - pontos;
        usuario.setScoreFinanceiro(Math.max(novoScore, 0));
    }

    private Transacao buscarTransacaoDoUsuario(Long id) {
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transação não encontrada."));

        if (!transacao.getUsuario().getId().equals(getUsuarioLogadoId())) {
            throw new NegocioException("Você não tem permissão para alterar esta transação.");
        }
        return transacao;
    }

    private Long getUsuarioLogadoId() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Long.parseLong(jwt.getSubject());
    }

    private Usuario getUsuarioLogado() {
        return usuarioRepository.findById(getUsuarioLogadoId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário logado não encontrado no banco."));
    }
}