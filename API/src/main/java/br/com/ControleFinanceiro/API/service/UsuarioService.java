package br.com.ControleFinanceiro.API.service;

import br.com.ControleFinanceiro.API.dto.request.AtualizarCredenciaisRequest;
import br.com.ControleFinanceiro.API.dto.request.AtualizarPerfilRequest;
import br.com.ControleFinanceiro.API.dto.response.UsuarioResponse;
import br.com.ControleFinanceiro.API.entity.Usuario;
import br.com.ControleFinanceiro.API.entity.emuns.TomIA;
import br.com.ControleFinanceiro.API.mapper.UsuarioMapper;
import br.com.ControleFinanceiro.API.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioResponse atualizarPerfilCompleto(AtualizarPerfilRequest dto) {
        Usuario usuario = getUsuarioLogado();

        usuario.setNome(dto.nome());
        usuario.setTomIA(TomIA.valueOf(dto.tomIA()));

        usuario = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuario);
    }

    @Transactional
    public void atualizarCredenciais(AtualizarCredenciaisRequest dto) {
        Usuario usuario = getUsuarioLogado();

        if (!usuario.getEmail().equals(dto.emailAtual())) {
            throw new RuntimeException("E-mail atual não confere.");
        }

        if (!passwordEncoder.matches(dto.senhaAtual(), usuario.getSenha())) {
            throw new RuntimeException("Senha atual incorreta.");
        }

        if (dto.novaSenha() != null && !dto.novaSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.novaSenha()));
        }

        if (dto.novoEmail() != null && !dto.novoEmail().isBlank()) {
            usuario.setEmail(dto.novoEmail());
        }

        usuarioRepository.save(usuario);
    }

    @Transactional
    public void desativarConta() {
        Usuario usuario = getUsuarioLogado();
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

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