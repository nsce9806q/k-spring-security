package org.swmaestro.kauth.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.swmaestro.kauth.core.jwt.JwtFilterChainBuilder;

/**
 * @author Kevin Park
 * @since 0.0.1
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = KSecurityConfigurationTests.SecurityConfiguration.class)
@WebAppConfiguration
public class KSecurityConfigurationTests {

    @Autowired
    SecurityFilterChain filterChain;

    @DisplayName("필터 체인 빌더 테스트")
    @Test
    public void filterChainBuilderTest() {
        // TODO(krapie): 아래 필터 빌더를 통해 생성된 필터 체인이 제대로 생성되었는지 확인
        filterChain.getFilters().forEach(System.out::println);
    }

    @Configuration
    @EnableWebSecurity
    @EnableWebMvc
    static class SecurityConfiguration {
        // TODO(krapie): 사용자(라이브러리 사용자)가 직접 구현해야 하는 부분을 여기에 선언

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
