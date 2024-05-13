package com.app.cars.security.service;

import com.app.cars.security.dto.RefreshTokenDto;
import com.app.cars.security.dto.TokensDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public interface TokensService {
    TokensDto generateToken(Authentication authentication);
    UsernamePasswordAuthenticationToken parseAccessToken(String token);
    TokensDto refreshTokens(RefreshTokenDto refreshTokenDto);
}
