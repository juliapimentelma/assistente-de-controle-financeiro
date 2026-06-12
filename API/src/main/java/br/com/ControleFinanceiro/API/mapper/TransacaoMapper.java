package br.com.ControleFinanceiro.API.mapper;

import br.com.ControleFinanceiro.API.dto.response.TransacaoResponse;
import br.com.ControleFinanceiro.API.entity.Transacao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransacaoMapper {

    @Mapping(source = "subcategoria.nome", target = "nomeSubcategoria")
    @Mapping(source = "subcategoria.categoria.nome", target = "nomeCategoriaMaior")
    @Mapping(source = "subcategoria.categoria.tipo", target = "tipo")
    TransacaoResponse toResponseDTO(Transacao transacao);
}
