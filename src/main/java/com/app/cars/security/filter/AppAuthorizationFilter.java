package com.app.cars.security.filter;

import com.app.cars.security.service.TokensService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

public class AppAuthorizationFilter extends BasicAuthenticationFilter {
    private final TokensService tokensService;

    public AppAuthorizationFilter(AuthenticationManager authenticationManager, TokensService tokensService) {
        super(authenticationManager);
        this.tokensService = tokensService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        var header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null) {
            var authorizedUser = tokensService.parseAccessToken(header);
            SecurityContextHolder.getContext().setAuthentication(authorizedUser);

        }
        chain.doFilter(request, response);
    }
}
