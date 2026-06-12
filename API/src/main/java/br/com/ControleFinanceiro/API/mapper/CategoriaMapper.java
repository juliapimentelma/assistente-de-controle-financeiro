package br.com.ControleFinanceiro.API.mapper;

import br.com.ControleFinanceiro.API.dto.response.CategoriaResponse;
import br.com.ControleFinanceiro.API.dto.response.SubcategoriaResponse;
import br.com.ControleFinanceiro.API.entity.Categoria;
import br.com.ControleFinanceiro.API.entity.Subcategoria;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    CategoriaResponse toResponseDTO(Categoria categoria);

    SubcategoriaResponse toSubcategoriaResponseDTO(Subcategoria subcategoria);
}
