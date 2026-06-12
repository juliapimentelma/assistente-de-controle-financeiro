package br.com.ControleFinanceiro.API.mapper;


import br.com.ControleFinanceiro.API.dto.response.UsuarioResponse;
import br.com.ControleFinanceiro.API.entity.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioResponse toResponseDTO(Usuario usuario);
}
