package com.app.cars.security.filter;

import com.app.cars.security.dto.AuthenticationDto;
import com.app.cars.security.service.TokensService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collections;

// domyslnie ten filter wywola metode attemptAuthentification kiedy na rzecz aplikacji wywola sie
// request POST -> /login
// domyslne URI mozna zmienic w konstruktorze klasy
public class AppAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final TokensService tokensService;
    private final AuthenticationManager authenticationManager;

    public AppAuthenticationFilter(TokensService tokensService, AuthenticationManager authenticationManager) {
        this.tokensService = tokensService;
        this.authenticationManager = authenticationManager;
        //setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST")); -> przykładowa zmiana URI

    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        var jsonBody = new ObjectMapper()
                .readValue(request.getInputStream(), AuthenticationDto.class);

        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                jsonBody.username(),
                jsonBody.password(),
                Collections.emptyList()
        ));
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        var tokens = tokensService.generateToken(authResult);

        Cookie accessTokenCookie = new Cookie("AccessToken", tokens.accessToken());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setMaxAge(86400);

        Cookie refreshTokenCookie = new Cookie("RefreshToken", tokens.refreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(86400);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        // mozna tez zwrocic tokeny w JSON Body (ponizej) lub jako header
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(new ObjectMapper().writeValueAsString(tokens));
        response.getWriter().flush();
        response.getWriter().close();
        
    }
}
