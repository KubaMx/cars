package com.app.cars.security.service.impl;

import com.app.cars.persistence.repository.UserEntityRepository;
import com.app.cars.security.dto.AuthenticationDto;
import com.app.cars.security.dto.RefreshTokenDto;
import com.app.cars.security.dto.TokensDto;
import com.app.cars.security.service.TokensService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
@RequiredArgsConstructor
public class TokensServiceImpl implements TokensService {

    @Value("${km-config.tokens.access.expiration_time_ms}")
    private Long AccessTokenExpirationTimeMs;
    @Value("${km-config.tokens.refresh.expiration_time_ms}")
    private Long RefreshTokenExpirationTimeMs;
    @Value("${km-config.tokens.refresh.access_token_expiration_time_ms_property}")
    private Long RefreshTokenProperty;
    @Value("${km-config.tokens.prefix}")
    private Long TokensPrefix;

    private final UserEntityRepository userEntityRepository;
    private final SecretKey secretKey;
    private final PasswordEncoder passwordEncoder;


    @Override
    public TokensDto generateToken(AuthenticationDto authenticationDto) {
        return null;
    }

    @Override
    public void parseTokens(String token) {

    }

    @Override
    public TokensDto refreshTokens(RefreshTokenDto refreshTokenDto) {
        return null;
    }
}
