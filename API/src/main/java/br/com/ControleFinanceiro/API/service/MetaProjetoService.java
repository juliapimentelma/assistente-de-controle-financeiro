package br.com.ControleFinanceiro.API.service;

import br.com.ControleFinanceiro.API.dto.request.AporteRequest;
import br.com.ControleFinanceiro.API.dto.request.MetaRequest;
import br.com.ControleFinanceiro.API.dto.response.MetaResponse;
import br.com.ControleFinanceiro.API.entity.MetaProjeto;
import br.com.ControleFinanceiro.API.entity.Usuario;
import br.com.ControleFinanceiro.API.entity.emuns.StatusMeta;
import br.com.ControleFinanceiro.API.exception.NegocioException;
import br.com.ControleFinanceiro.API.mapper.MetaMapper;
import br.com.ControleFinanceiro.API.repository.MetaProjetoRepository;
import br.com.ControleFinanceiro.API.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MetaProjetoService {

    private final MetaProjetoRepository metaRepository;
    private final UsuarioRepository usuarioRepository;
    private final MetaMapper metaMapper;

    public List<MetaResponse> listarMetas() {
        Long usuarioId = getUsuarioLogadoId();
        return metaRepository.findAllByUsuarioId(usuarioId).stream()
                .map(metaMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public MetaResponse criar(MetaRequest dto) {
        Usuario usuario = getUsuarioLogado();

        MetaProjeto meta = MetaProjeto.builder()
                .titulo(dto.titulo())
                .valorAlvo(dto.valorAlvo())
                .valorAtual(BigDecimal.ZERO)
                .status(StatusMeta.EM_ANDAMENTO)
                .usuario(usuario)
                .build();

        meta = metaRepository.save(meta);
        return metaMapper.toResponseDTO(meta);
    }

    @Transactional
    public MetaResponse realizarAporte(Long id, AporteRequest dto) {
        MetaProjeto meta = buscarMetaDoUsuario(id);

        if (meta.getStatus() == StatusMeta.CONCLUIDA) {
            throw new NegocioException("Esta meta já foi concluída! Parabéns!");
        }

        BigDecimal novoValorAtual = meta.getValorAtual().add(dto.valor());
        meta.setValorAtual(novoValorAtual);

        if (novoValorAtual.compareTo(meta.getValorAlvo()) >= 0) {
            meta.setStatus(StatusMeta.CONCLUIDA);
            meta.setValorAtual(meta.getValorAlvo());

            Usuario usuario = meta.getUsuario();
            usuario.setScoreFinanceiro(usuario.getScoreFinanceiro() + 20);
            usuarioRepository.save(usuario);
        }

        meta = metaRepository.save(meta);
        return metaMapper.toResponseDTO(meta);
    }

    @Transactional
    public void deletar(Long id) {
        MetaProjeto meta = buscarMetaDoUsuario(id);
        metaRepository.delete(meta);
    }

    private MetaProjeto buscarMetaDoUsuario(Long id) {
        MetaProjeto meta = metaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meta não encontrada."));

        if (!meta.getUsuario().getId().equals(getUsuarioLogadoId())) {
            throw new NegocioException("Você não tem permissão para acessar esta meta.");
        }
        return meta;
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