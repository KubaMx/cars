package com.app.cars.security.config;

import com.app.cars.security.config.dto.AuthenticationErrorDto;
import com.app.cars.security.filter.AppAuthenticationFilter;
import com.app.cars.security.filter.AppAuthorizationFilter;
import com.app.cars.security.service.TokensService;
import com.app.cars.security.service.impl.AppUserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class AppWebSecurityConfig {
    private final AppUserDetailsServiceImpl appUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final TokensService tokensService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        var authenticationManagerBuilder =
                httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(appUserDetailsService)
                .passwordEncoder(passwordEncoder);

        var authenticationManager = authenticationManagerBuilder.build();

        httpSecurity
                .cors(httpSecurityCorsConfigurer -> {

                })
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
                    httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(accessDeniedHandler());
                    httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(authenticationEntryPoint());
                })
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                    httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.POST, "/users/**", "/login")
                            .permitAll()
                            .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                            .anyRequest()
                            .authenticated())
                .addFilter(new AppAuthenticationFilter(tokensService, authenticationManager))
                .addFilter(new AppAuthorizationFilter(tokensService, authenticationManager))
                .authenticationManager(authenticationManager);

        return httpSecurity.build();
    }

    private AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            var ex = new AuthenticationErrorDto(authException.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(new ObjectMapper().writeValueAsString(ex));
            response.getWriter().flush();
            response.getWriter().close();
        };
    }

    private AccessDeniedHandler accessDeniedHandler() {
        return (request, response, authException) -> {
            var ex = new AuthenticationErrorDto(authException.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(new ObjectMapper().writeValueAsString(ex));
            response.getWriter().flush();
            response.getWriter().close();
        };
    }

}
