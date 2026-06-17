package br.com.ControleFinanceiro.API.dto.response;

public record SubcategoriaResponse(
        Long id,
        String nome,
        Long idCategoria
) {}
