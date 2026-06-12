package br.com.ControleFinanceiro.API.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubcategoriaRequest(
        @NotBlank(message = "O nome da subcategoria é obrigatório")
        String nome,

        @NotNull(message = "O ID da categoria pai é obrigatório")
        Long categoriaId
) {}
