package org.swmaestro.kauth.core;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.swmaestro.kauth.authentication.UsernamePasswordAuthenticationManager;

public abstract class KauthFilterChainBuilder<T> {

    protected final HttpSecurity httpSecurity;

    protected final UsernamePasswordAuthenticationManager authenticationManager;

    protected KauthFilterChainBuilder(HttpSecurity httpSecurity,
        UsernamePasswordAuthenticationManager authenticationManager) {
        this.httpSecurity = httpSecurity;
        this.authenticationManager = authenticationManager;
    }

    public abstract T init() throws Exception;
}
