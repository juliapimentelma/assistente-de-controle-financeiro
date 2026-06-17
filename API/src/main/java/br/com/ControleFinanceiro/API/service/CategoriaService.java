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
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final SubcategoriaRepository subcategoriaRepository;
    private final CategoriaMapper categoriaMapper;


    // O Transactional é obrigatório para o Java conseguir ler a lista (Lazy Loading) sem dar erro!
    @Transactional(readOnly = true)
    public List<CategoriaResponse> listarCategoriasDoUsuario() {
        Long usuarioId = getUsuarioLogadoId();

        // 1. Busca apenas as categorias pai (categoria_pai_id IS NULL)
        return categoriaRepository.findAllByUsuarioIdAndCategoriaPaiIsNull(usuarioId).stream()
                .map(categoria -> new CategoriaResponse(
                        categoria.getId(),
                        categoria.getNome(),
                        // 2. Transforma as categorias filhas em SubcategoriaResponse para o Angular ler
                        categoria.getSubcategorias().stream()
                                .map(sub -> new SubcategoriaResponse(
                                        sub.getId(),
                                        sub.getNome(),
                                        categoria.getId() // ID do pai
                                ))
                                .toList()
                ))
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

    public List<CategoriaResponse> buscarCategoriasComSubcategorias() {
        return categoriaRepository.findByCategoriaPaiIsNull().stream()
                .map(c -> new CategoriaResponse(
                        c.getId(),
                        c.getNome(),
                        // AQUI ESTAVA O ERRO: Precisamos criar um SubcategoriaResponse, e não CategoriaResponse!
                        c.getSubcategorias().stream()
                                .map(sub -> new SubcategoriaResponse(
                                        sub.getId(),
                                        sub.getNome(),
                                        c.getId()
                                ))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }
}