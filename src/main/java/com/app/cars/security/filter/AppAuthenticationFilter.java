package com.app.cars.security.filter;

import com.app.cars.security.dto.AuthenticationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collections;

// domyslnie ten filter wywola metode attemptAuthentification kiedy na rzecz aplikacji wywola sie
// request POST -> /login
// domyslne URI mozna zmienic w konstruktorze klasy
public class AppAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AppAuthenticationFilter(TokenService tokenService, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
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
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
    }
}
