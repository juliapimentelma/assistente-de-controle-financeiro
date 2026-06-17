package br.com.ControleFinanceiro.API.dto.response;

import br.com.ControleFinanceiro.API.entity.emuns.TipoCategoria;

import java.util.List;

public record CategoriaResponse(
        Long id,
        String nome,
        // Pode descomentar esses campos abaixo se eles existirem na sua entidade Categoria!
        // TipoCategoria tipo,
        // String corHex,
        // String icone,
        List<SubcategoriaResponse> subcategorias
) {}