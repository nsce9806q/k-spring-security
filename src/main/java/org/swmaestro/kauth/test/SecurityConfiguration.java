package org.swmaestro.kauth.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.swmaestro.kauth.core.jwt.JwtFilterChainBuilder;

/**
 * TODO Release전에 지워야 함
 * 테스트 용
 */
@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(JwtFilterChainBuilder builder) throws Exception {
        return builder.init()
            .UsernamePassword("/login")
            .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
