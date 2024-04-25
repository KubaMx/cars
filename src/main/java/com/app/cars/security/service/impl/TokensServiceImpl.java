package com.app.cars.security.service.impl;

import com.app.cars.persistence.repository.UserEntityRepository;
import com.app.cars.security.dto.AuthenticationDto;
import com.app.cars.security.dto.RefreshTokenDto;
import com.app.cars.security.dto.TokensDto;
import com.app.cars.security.service.TokensService;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokensServiceImpl implements TokensService {

    @Value("${km-config.tokens.access.expiration_time_ms}")
    private Long accessTokenExpirationTimeMs;
    @Value("${km-config.tokens.refresh.expiration_time_ms}")
    private Long refreshTokenExpirationTimeMs;
    @Value("${km-config.tokens.refresh.access_token_expiration_time_ms_property}")
    private String refreshTokenProperty;
    @Value("${km-config.tokens.prefix}")
    private String tokensPrefix;

    private final UserEntityRepository userEntityRepository;
    private final SecretKey secretKey;
    private final PasswordEncoder passwordEncoder;


    @Override
    public TokensDto generateToken(AuthenticationDto authenticationDto) {
        var userFromDb = userEntityRepository
                .findByUsername(authenticationDto.username())
                .orElseThrow(() ->new IllegalStateException("Authentication failed [1]"));

        if(!passwordEncoder.matches(
                authenticationDto.password(),
                userFromDb.getPassword()
        )) {
            throw new IllegalStateException("Authentication failed [2]");
        }

        var userId = userFromDb.getId();
        var currentDate = new Date();
        var accessTokenExpirationDate = new Date(currentDate.getTime() + accessTokenExpirationTimeMs);
        var refreshTokenExpirationDate = new Date(currentDate.getTime() + refreshTokenExpirationTimeMs);

        var accessToken = Jwts
                .builder()
                .subject(userId + "")
                .expiration(accessTokenExpirationDate)
                .issuedAt(currentDate)
                .signWith(secretKey)
                .compact();

        var refreshToken = Jwts
                .builder()
                .subject(userId + "")
                .expiration(refreshTokenExpirationDate)
                .issuedAt(currentDate)
                .claim(refreshTokenProperty, accessTokenExpirationDate.getTime() )
                .signWith(secretKey)
                .compact();

        return new TokensDto(accessToken, refreshToken);
    }

    @Override
    public void parseTokens(String token) {

    }

    @Override
    public TokensDto refreshTokens(RefreshTokenDto refreshTokenDto) {
        return null;
    }
}
