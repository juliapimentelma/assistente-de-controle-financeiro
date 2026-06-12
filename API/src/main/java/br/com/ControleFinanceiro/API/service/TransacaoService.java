package br.com.ControleFinanceiro.API.service;

import br.com.ControleFinanceiro.API.dto.request.TransacaoRequest;
import br.com.ControleFinanceiro.API.dto.response.TransacaoResponse;
import br.com.ControleFinanceiro.API.entity.Orcamento;
import br.com.ControleFinanceiro.API.entity.Subcategoria;
import br.com.ControleFinanceiro.API.entity.Transacao;
import br.com.ControleFinanceiro.API.entity.Usuario;
import br.com.ControleFinanceiro.API.entity.emuns.StatusTransacao;
import br.com.ControleFinanceiro.API.entity.emuns.TipoCategoria;
import br.com.ControleFinanceiro.API.exception.NegocioException;
import br.com.ControleFinanceiro.API.mapper.TransacaoMapper;
import br.com.ControleFinanceiro.API.repository.OrcamentoRepository;
import br.com.ControleFinanceiro.API.repository.SubcategoriaRepository;
import br.com.ControleFinanceiro.API.repository.TransacaoRepository;
import br.com.ControleFinanceiro.API.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SubcategoriaRepository subcategoriaRepository;
    private final OrcamentoRepository orcamentoRepository;
    private final TransacaoMapper transacaoMapper;

    @Transactional
    public TransacaoResponse criar(TransacaoRequest dto) {
        Usuario usuario = getUsuarioLogado();

        Subcategoria subcategoria = subcategoriaRepository.findById(dto.subcategoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Subcategoria não encontrada."));

        if (!subcategoria.getCategoria().getUsuario().getId().equals(usuario.getId())) {
            throw new NegocioException("Esta subcategoria não pertence a você.");
        }

        Transacao transacao = Transacao.builder()
                .descricao(dto.descricao())
                .valor(dto.valor())
                .dataCriacao(LocalDate.now())
                .dataVencimento(dto.dataVencimento())
                .mesCompetencia(dto.mesCompetencia())
                .anoCompetencia(dto.anoCompetencia())
                .status(dto.status())
                .subcategoria(subcategoria)
                .usuario(usuario)
                .build();

        if (transacao.getStatus() == StatusTransacao.PAGO) {
            processarPagamento(usuario, transacao);
        }

        transacao = transacaoRepository.save(transacao);
        usuarioRepository.save(usuario);

        return transacaoMapper.toResponseDTO(transacao);
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