package org.swmaestro.kauth.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.swmaestro.kauth.authentication.UsernamePasswordAuthenticationManager;
import org.swmaestro.kauth.core.jwt.JwtFilterChainBuilder;
import org.swmaestro.kauth.util.JwtUtil;

@Configuration
public class KauthConfiguration {

    @Bean
    public JwtFilterChainBuilder jwtFilterChainBuilder(HttpSecurity httpSecurity, JwtUtil jwtUtil,
        UsernamePasswordAuthenticationManager authenticationManager) {
        return new JwtFilterChainBuilder(httpSecurity, jwtUtil, authenticationManager);
    }
}
