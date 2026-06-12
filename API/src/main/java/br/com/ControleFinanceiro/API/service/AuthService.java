package br.com.ControleFinanceiro.API.service;

import br.com.ControleFinanceiro.API.dto.request.LoginRequest;
import br.com.ControleFinanceiro.API.dto.request.UsuarioRequest;
import br.com.ControleFinanceiro.API.dto.response.TokenResponse;
import br.com.ControleFinanceiro.API.dto.response.UsuarioResponse;
import br.com.ControleFinanceiro.API.entity.Categoria;
import br.com.ControleFinanceiro.API.entity.Usuario;
import br.com.ControleFinanceiro.API.entity.emuns.TipoCategoria;
import br.com.ControleFinanceiro.API.entity.emuns.TomIA;
import br.com.ControleFinanceiro.API.mapper.UsuarioMapper;
import br.com.ControleFinanceiro.API.repository.CategoriaRepository;
import br.com.ControleFinanceiro.API.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final UsuarioMapper usuarioMapper;

    @Transactional
    public UsuarioResponse registrar(UsuarioRequest dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new DataIntegrityViolationException("Este e-mail já está cadastrado.");
        }

        Usuario usuario = Usuario.builder()
                .nome(dto.nome())
                .email(dto.email())
                .senha(passwordEncoder.encode(dto.senha()))
                .saldoAtual(BigDecimal.ZERO)
                .scoreFinanceiro(500)
                .tomIA(TomIA.CONSELHEIRO)
                .modoEscuro(true)
                .tutorialConcluido(false)
                .dataCadastro(LocalDate.now())
                .build();

        usuario = usuarioRepository.save(usuario);
        criarCategoriasPadrao(usuario);

        return usuarioMapper.toResponseDTO(usuario);
    }

    public TokenResponse login(LoginRequest dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new BadCredentialsException("E-mail ou senha inválidos."));

        if (!passwordEncoder.matches(dto.senha(), usuario.getSenha())) {
            throw new BadCredentialsException("E-mail ou senha inválidos.");
        }

        String token = gerarToken(usuario);

        return new TokenResponse(token, usuarioMapper.toResponseDTO(usuario));
    }

    private void criarCategoriasPadrao(Usuario usuario) {
        List<Categoria> categoriasBase = List.of(
                Categoria.builder().nome("Receitas").tipo(TipoCategoria.RECEITA).usuario(usuario).build(),
                Categoria.builder().nome("Investimentos").tipo(TipoCategoria.INVESTIMENTO).usuario(usuario).build(),
                Categoria.builder().nome("Despesas Fixas").tipo(TipoCategoria.DESPESA).usuario(usuario).build(),
                Categoria.builder().nome("Despesas Variáveis").tipo(TipoCategoria.DESPESA).usuario(usuario).build(),
                Categoria.builder().nome("Despesas Extras").tipo(TipoCategoria.DESPESA).usuario(usuario).build()
        );
        categoriaRepository.saveAll(categoriasBase);
    }

    private String gerarToken(Usuario usuario) {
        Instant agora = Instant.now();
        long tempoExpiracaoSegundos = 86400L;

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("conselheiro-api")
                .subject(usuario.getId().toString())
                .issuedAt(agora)
                .expiresAt(agora.plusSeconds(tempoExpiracaoSegundos))
                .claim("nome", usuario.getNome())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
