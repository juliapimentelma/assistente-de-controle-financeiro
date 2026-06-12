package br.com.ControleFinanceiro.API.mapper;

import br.com.ControleFinanceiro.API.dto.response.MetaResponse;
import br.com.ControleFinanceiro.API.entity.MetaProjeto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MetaMapper {
    MetaResponse toResponseDTO(MetaProjeto meta);
}
