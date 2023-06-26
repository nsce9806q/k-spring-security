package org.swmaestro.kauth.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.swmaestro.kauth.KauthApplication;
import org.swmaestro.kauth.core.jwt.JwtFilterChainBuilder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Kevin Park
 * @since 0.0.1
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { KSecurityConfigurationTests.SecurityConfiguration.class, KauthApplication.class })
public class KSecurityConfigurationTests {

    @Autowired
    private ApplicationContext context;

    @DisplayName("SecurityConfiguration 에 JwtUsernamePasswordAuthenticationFilter 가 등록되어 있는지 확인")
    @Test
    public void checkJwtUsernamePasswordAuthenticationFilterIsRegisteredInSecurityConfiguration() {
        assertThat(context.getBean(SecurityFilterChain.class)
                .getFilters()
                .stream()
                .anyMatch(filter -> filter.getClass().getName().contains("JwtUsernamePasswordAuthenticationFilter")))
                .isTrue();
    }

    @Configuration
    @EnableWebSecurity
    static class SecurityConfiguration {
        @Bean
        SecurityFilterChain filterChain(JwtFilterChainBuilder builder) throws Exception {
            return builder.init()
                    .UsernamePassword("/login")
                    .build();
        }

        @Bean
        public BCryptPasswordEncoder bCryptPasswordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        UserDetailsService userDetailsService() {
            UserDetails user = User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build();
            return new InMemoryUserDetailsManager(user);
        }
    }
}
