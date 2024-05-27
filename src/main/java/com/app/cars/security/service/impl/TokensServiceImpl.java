package com.app.cars.security.service.impl;

import com.app.cars.persistence.repository.UserEntityRepository;
import com.app.cars.security.dto.RefreshTokenDto;
import com.app.cars.security.dto.TokensDto;
import com.app.cars.security.service.TokensService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

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
    public TokensDto generateToken(Authentication authentication) {
        var userFromDb = userEntityRepository
                .findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Authentication failed [1]"));

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
                .claim(refreshTokenProperty, accessTokenExpirationDate.getTime())
                .signWith(secretKey)
                .compact();

        return new TokensDto(accessToken, refreshToken);
    }

    @Override
    public UsernamePasswordAuthenticationToken parseAccessToken(String header) {
        if (!header.startsWith(tokensPrefix)) {
            throw new IllegalArgumentException("Authorization header incorrect format");
        }

        var token = header.replaceAll(tokensPrefix, "");

        // TESTOWO WYCIAGAM DANE
        System.out.println("---------------------------------------");
        System.out.println(getId(token));
        System.out.println(getExpirationDate(token));
        System.out.println("---------------------------------------");

        if (isTokenNotValid(token)) {
            throw new IllegalArgumentException("Authorization header is not valid");
        }

        var userId = getId(token);

        return userEntityRepository
                .findById(userId)
                .map(userFromDb -> {
                    var userDto = userFromDb.toUsernamePasswordAuthenticationTokenDto();
                    return new UsernamePasswordAuthenticationToken(
                            userDto.username(),
                            null,
                            List.of(new SimpleGrantedAuthority(userDto.role()))
                    );
                }).orElseThrow();
    }


    @Override
    public TokensDto refreshTokens(RefreshTokenDto refreshTokenDto) {
        return null;
    }

    // METODY POMOCNICZE DO WYCIĄGANIA INFORMACJI Z TOKENA

    private Claims claims(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Long getId(String token) {
        return Long.parseLong(claims(token).getSubject());
    }

    private Date getExpirationDate(String token) {
        return claims(token).getExpiration();
    }

    private boolean isTokenNotValid(String token) {
        return getExpirationDate(token).before(new Date());
    }

    private Long getAccessTokenExpirationTimeMs(String token) {
        return claims(token).get(refreshTokenProperty, Long.class);
    }
}
