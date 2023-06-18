package org.swmaestro.kauth.showcase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.swmaestro.kauth.core.jwt.JwtFilterChainBuilder;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Kevin Park
 * @since 0.0.1
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserLoginWithJwtTests.SecurityConfiguration.class)
@WebAppConfiguration
public class UserLoginWithJwtTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();
    }

    @DisplayName("Username + Password (JWT) 로그인 성공 테스트")
    @Test
    public void withUsernamePasswordJwtLoginSuccessTest() throws Exception {
        String requestJson = "{\"username\":\"user\", \"password\": \"password\"}";

        // login with mock user with url /login;
        this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())

                // get response and check if there is refresh token and access token in header
                .andExpect(authenticated())
                .andExpect(status().is2xxSuccessful())
                .andExpect(header().exists("Authorization"))
                .andExpect(header().exists("Refresh-Token"));
    }

    @DisplayName("Username + Password (JWT) 로그인 실패 테스트")
    @Test
    public void withUsernamePasswordJwtLoginFailTest() throws Exception {
        String requestJson = "{\"username\":\"user\", \"password\": \"wrongPassword\"}";

        // login with mock user with url /login
        this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())

                // get response and check if there is refresh token and access token in header
                .andExpect(unauthenticated())
                .andExpect(status().is4xxClientError());

                // TODO(krapie): 다양한 에러 상황 assert
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
