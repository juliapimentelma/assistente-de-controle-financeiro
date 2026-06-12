package br.com.ControleFinanceiro.API.service;

import br.com.ControleFinanceiro.API.dto.request.OrcamentoRequest;
import br.com.ControleFinanceiro.API.dto.response.OrcamentoResponse;
import br.com.ControleFinanceiro.API.entity.Categoria;
import br.com.ControleFinanceiro.API.entity.Orcamento;
import br.com.ControleFinanceiro.API.entity.Usuario;
import br.com.ControleFinanceiro.API.exception.NegocioException;
import br.com.ControleFinanceiro.API.mapper.OrcamentoMapper;
import br.com.ControleFinanceiro.API.repository.CategoriaRepository;
import br.com.ControleFinanceiro.API.repository.OrcamentoRepository;
import br.com.ControleFinanceiro.API.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrcamentoService {

    private final OrcamentoRepository orcamentoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final OrcamentoMapper orcamentoMapper;

    public List<OrcamentoResponse> listarPorMesEAno(Integer mes, Integer ano) {
        Long usuarioId = getUsuarioLogadoId();

        return orcamentoRepository.findByUsuarioIdAndMesAndAno(usuarioId, mes, ano).stream()
                .map(orcamentoMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public OrcamentoResponse definirOrcamento(OrcamentoRequest dto) {
        Long usuarioId = getUsuarioLogadoId();

        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada."));

        if (!categoria.getUsuario().getId().equals(usuarioId)) {
            throw new NegocioException("Você não tem permissão para definir orçamento nesta categoria.");
        }

        Optional<Orcamento> orcamentoExistente = orcamentoRepository
                .findByUsuarioIdAndCategoriaIdAndMesAndAno(usuarioId, categoria.getId(), dto.mes(), dto.ano());

        Orcamento orcamento;

        if (orcamentoExistente.isPresent()) {

            orcamento = orcamentoExistente.get();
            orcamento.setValorPlanejado(dto.valorPlanejado());
        } else {
            Usuario usuarioRef = usuarioRepository.getReferenceById(usuarioId);

            orcamento = Orcamento.builder()
                    .mes(dto.mes())
                    .ano(dto.ano())
                    .valorPlanejado(dto.valorPlanejado())
                    .categoria(categoria)
                    .usuario(usuarioRef)
                    .build();
        }

        orcamento = orcamentoRepository.save(orcamento);
        return orcamentoMapper.toResponseDTO(orcamento);
    }

    private Long getUsuarioLogadoId() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Long.parseLong(jwt.getSubject());
    }
}
