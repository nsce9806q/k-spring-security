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
import org.swmaestro.kauth.KauthApplication;
import org.swmaestro.kauth.core.jwt.JwtFilterChainBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Kevin Park
 * @since 0.0.1
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { UserLoginWithJwtTests.SecurityConfiguration.class, KauthApplication.class })
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

        this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())

                .andExpect(authenticated())
                .andExpect(status().is2xxSuccessful())
                .andExpect(header().exists("Authorization"))
                .andExpect(header().exists("Refresh-Token"));
    }

    @DisplayName("Username + Password (JWT) 로그인: 패스워드 실패 테스트")
    @Test
    public void withUsernamePasswordJwtLoginFailByWrongPasswordTest() throws Exception {
        String requestJson = "{\"username\":\"user\", \"password\": \"wrongPassword\"}";

        this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())

                .andExpect(unauthenticated())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Username + Password (JWT) 로그인: 만료된 유저 실패 테스트")
    @Test
    public void withUsernamePasswordJwtLoginFailByExpiredUserTest() throws Exception {
        String requestJson = "{\"username\":\"expiredUser\", \"password\": \"password\"}";

        this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())

                .andExpect(unauthenticated())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Username + Password (JWT) 로그인: 잠긴 유저 실패 테스트")
    @Test
    public void withUsernamePasswordJwtLoginFailByLockedUserTest() throws Exception {
        String requestJson = "{\"username\":\"lockedUser\", \"password\": \"password\"}";

        this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())

                .andExpect(unauthenticated())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Username + Password (JWT) 로그인: 인증 만료 유저 실패 테스트")
    @Test
    public void withUsernamePasswordJwtLoginFailByCredentialExpiredUserTest() throws Exception {
        String requestJson = "{\"username\":\"credentialExpiredUser\", \"password\": \"password\"}";

        this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())

                .andExpect(unauthenticated())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Username + Password (JWT) 로그인: 비활성화 유저 실패 테스트")
    @Test
    public void withUsernamePasswordJwtLoginFailByDisabledUserTest() throws Exception {
        String requestJson = "{\"username\":\"disabledUser\", \"password\": \"password\"}";

        this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())

                .andExpect(unauthenticated())
                .andExpect(status().isUnauthorized());
    }

    @Configuration
    @EnableWebSecurity
    @EnableWebMvc
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
            UserDetails user = User.withDefaultPasswordEncoder()
                    .username("user")
                    .password("password")
                    .roles("USER")
                    .build();

            UserDetails expiredUser = User.withDefaultPasswordEncoder()
                    .username("expiredUser")
                    .password("password")
                    .roles("USER")
                    .accountExpired(true)
                    .build();

            UserDetails lockedUser = User.withDefaultPasswordEncoder()
                    .username("lockedUser")
                    .password("password")
                    .roles("USER")
                    .accountLocked(true)
                    .build();

            UserDetails credentialExpiredUser = User.withDefaultPasswordEncoder()
                    .username("credentialExpiredUser")
                    .password("password")
                    .roles("USER")
                    .credentialsExpired(true)
                    .build();

            UserDetails disabledUser = User.withDefaultPasswordEncoder()
                    .username("disabledUser")
                    .password("password")
                    .roles("USER")
                    .disabled(true)
                    .build();

            return new InMemoryUserDetailsManager(user, expiredUser, lockedUser, credentialExpiredUser, disabledUser);
        }
    }
}
