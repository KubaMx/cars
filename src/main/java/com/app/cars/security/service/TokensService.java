package com.app.cars.security.service;

import com.app.cars.security.dto.AuthenticationDto;
import com.app.cars.security.dto.RefreshTokenDto;
import com.app.cars.security.dto.TokensDto;

public interface TokensService {
    TokensDto generateToken(AuthenticationDto authenticationDto);
    void parseTokens(String token);
    TokensDto refreshTokens(RefreshTokenDto refreshTokenDto);
}
