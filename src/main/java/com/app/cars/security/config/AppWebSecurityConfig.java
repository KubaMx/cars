package com.app.cars.security.config;

import com.app.cars.security.service.TokensService;
import com.app.cars.security.service.impl.AppUserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class AppWebSecurityConfig {
    private final AppUserDetailsServiceImpl appUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final TokensService tokensService;


}
