package org.swmaestro.kauth.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.swmaestro.kauth.core.JwtConfigurer;

/**
 * TODO Release전에 지워야 함
 * 테스트 용
 */
@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        JwtConfigurer.init(http).complete();

        return http.build();
    }


}
