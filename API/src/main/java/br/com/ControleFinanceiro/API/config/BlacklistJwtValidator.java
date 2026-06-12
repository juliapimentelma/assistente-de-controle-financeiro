package br.com.ControleFinanceiro.API.config;

import br.com.ControleFinanceiro.API.service.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlacklistJwtValidator implements OAuth2TokenValidator<Jwt> {

    private final TokenBlacklistService tokenBlacklistService;

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        if (tokenBlacklistService.isBloqueado(jwt.getTokenValue())) {
            OAuth2Error erro = new OAuth2Error("invalid_token", "O token foi revogado devido a um logout.", null);
            return OAuth2TokenValidatorResult.failure(erro);
        }
        return OAuth2TokenValidatorResult.success();
    }
}