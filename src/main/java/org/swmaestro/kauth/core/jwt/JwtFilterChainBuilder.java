package org.swmaestro.kauth.core.jwt;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.swmaestro.kauth.authentication.UsernamePasswordAuthenticationManager;
import org.swmaestro.kauth.core.KauthFilterChainBuilder;
import org.swmaestro.kauth.util.JwtUtil;

public class JwtFilterChainBuilder extends KauthFilterChainBuilder<JwtFilterChain> {

    private final JwtUtil jwtUtil;

    public JwtFilterChainBuilder(HttpSecurity httpSecurity, JwtUtil jwtUtil, UsernamePasswordAuthenticationManager authenticationManager) {
        super(httpSecurity, authenticationManager);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public JwtFilterChain init() throws Exception {
        return new JwtFilterChain(httpSecurity, jwtUtil, authenticationManager);
    }
}
