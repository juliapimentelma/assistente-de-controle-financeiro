package br.com.ControleFinanceiro.API.service;

import br.com.ControleFinanceiro.API.dto.response.UsuarioResponse;
import br.com.ControleFinanceiro.API.entity.Usuario;
import br.com.ControleFinanceiro.API.entity.emuns.TomIA;
import br.com.ControleFinanceiro.API.mapper.UsuarioMapper;
import br.com.ControleFinanceiro.API.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioResponse obterPerfilLogado() {
        Usuario usuario = getUsuarioLogado();
        return usuarioMapper.toResponseDTO(usuario);
    }

    @Transactional
    public UsuarioResponse atualizarTomIa(TomIA novoTom) {
        Usuario usuario = getUsuarioLogado();
        usuario.setTomIA(novoTom);
        usuario = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuario);
    }

    @Transactional
    public UsuarioResponse atualizarModoEscuro(Boolean modoEscuro) {
        Usuario usuario = getUsuarioLogado();
        usuario.setModoEscuro(modoEscuro);
        usuario = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuario);
    }

    private Usuario getUsuarioLogado() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long usuarioId = Long.parseLong(jwt.getSubject());

        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário logado não encontrado no banco."));
    }
}