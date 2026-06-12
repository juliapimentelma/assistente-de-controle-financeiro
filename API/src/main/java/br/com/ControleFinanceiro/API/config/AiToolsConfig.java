package br.com.ControleFinanceiro.API.config;

import br.com.ControleFinanceiro.API.dto.request.AnaliseCompraRequest;
import br.com.ControleFinanceiro.API.dto.response.AnaliseCompraResponse;
import br.com.ControleFinanceiro.API.entity.Usuario;
import br.com.ControleFinanceiro.API.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.math.BigDecimal;
import java.util.function.Function;

@Configuration
public class AiToolsConfig {

    @Bean
    @Description("Obtém o cenário financeiro atual do usuário logado para avaliar o impacto de uma nova compra.")
    public Function<AnaliseCompraRequest, AnaliseCompraResponse> analisarImpactoFinanceiro(UsuarioRepository usuarioRepository) {
        return request -> {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long usuarioId = Long.parseLong(jwt.getSubject());

            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

            BigDecimal valorCompra = BigDecimal.valueOf(request.valor());
            BigDecimal saldoAposCompra = usuario.getSaldoAtual().subtract(valorCompra);
            boolean vaiFicarNegativado = saldoAposCompra.compareTo(BigDecimal.ZERO) < 0;

            return new AnaliseCompraResponse(
                    usuario.getSaldoAtual(),
                    saldoAposCompra,
                    vaiFicarNegativado
            );
        };
    }
}