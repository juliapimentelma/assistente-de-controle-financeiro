package br.com.ControleFinanceiro.API.service;

import br.com.ControleFinanceiro.API.dto.request.SubcategoriaRequest;
import br.com.ControleFinanceiro.API.dto.response.CategoriaResponse;
import br.com.ControleFinanceiro.API.dto.response.SubcategoriaResponse;
import br.com.ControleFinanceiro.API.entity.Categoria;
import br.com.ControleFinanceiro.API.entity.Subcategoria;
import br.com.ControleFinanceiro.API.exception.NegocioException;
import br.com.ControleFinanceiro.API.mapper.CategoriaMapper;
import br.com.ControleFinanceiro.API.repository.CategoriaRepository;
import br.com.ControleFinanceiro.API.repository.SubcategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final SubcategoriaRepository subcategoriaRepository;
    private final CategoriaMapper categoriaMapper;


    public List<CategoriaResponse> listarCategoriasDoUsuario() {
        Long usuarioId = getUsuarioLogadoId();

        return categoriaRepository.findAllByUsuarioId(usuarioId).stream()
                .map(categoriaMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public SubcategoriaResponse criarSubcategoria(SubcategoriaRequest dto) {
        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria pai não encontrada."));

        if (!categoria.getUsuario().getId().equals(getUsuarioLogadoId())) {
            throw new NegocioException("Você não tem permissão para alterar as categorias deste usuário.");
        }

        boolean nomeJaExiste = categoria.getSubcategorias().stream()
                .anyMatch(sub -> sub.getNome().equalsIgnoreCase(dto.nome()));

        if (nomeJaExiste) {
            throw new NegocioException("Já existe uma subcategoria com este nome neste grupo.");
        }

        Subcategoria subcategoria = Subcategoria.builder()
                .nome(dto.nome())
                .categoria(categoria)
                .build();

        subcategoria = subcategoriaRepository.save(subcategoria);

        return categoriaMapper.toSubcategoriaResponseDTO(subcategoria);
    }

    private Long getUsuarioLogadoId() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Long.parseLong(jwt.getSubject());
    }
}