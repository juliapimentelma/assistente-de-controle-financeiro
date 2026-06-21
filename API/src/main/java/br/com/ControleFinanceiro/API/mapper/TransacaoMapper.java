package br.com.ControleFinanceiro.API.mapper;

import br.com.ControleFinanceiro.API.dto.response.TransacaoResponse;
import br.com.ControleFinanceiro.API.entity.Transacao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransacaoMapper {

    @Mapping(source = "categoria.nome", target = "nomeCategoriaMaior")
    @Mapping(source = "categoria.tipo", target = "tipo")
    @Mapping(source = "subcategoria.nome", target = "nomeSubcategoria")
    TransacaoResponse toResponseDTO(Transacao transacao);
}