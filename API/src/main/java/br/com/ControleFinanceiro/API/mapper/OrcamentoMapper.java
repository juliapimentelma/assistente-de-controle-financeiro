package br.com.ControleFinanceiro.API.mapper;


import br.com.ControleFinanceiro.API.dto.response.OrcamentoResponse;
import br.com.ControleFinanceiro.API.entity.Orcamento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrcamentoMapper {

    @Mapping(source = "categoria.id", target = "categoriaId")
    @Mapping(source = "categoria.nome", target = "nomeCategoria")
    @Mapping(source = "categoria.tipo", target = "tipoCategoria")
    OrcamentoResponse toResponseDTO(Orcamento orcamento);
}
