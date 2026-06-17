package br.com.ControleFinanceiro.API.dto.response;

import br.com.ControleFinanceiro.API.entity.emuns.TipoCategoria;

import java.util.List;

public record CategoriaResponse(
        Long id,
        String nome,
        List<CategoriaResponse> subcategorias
) {}