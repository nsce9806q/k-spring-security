package org.swmaestro.kauth.core.jwt;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.swmaestro.kauth.authentication.JwtUsernamePasswordAuthenticationFilter;
import org.swmaestro.kauth.authentication.UsernamePasswordAuthenticationManager;
import org.swmaestro.kauth.core.KauthFilterChain;
import org.swmaestro.kauth.util.JwtUtil;


/**
 * 인증/인가 전략: JWT
 * @author ChangEn Yea
 */
public class JwtFilterChain extends KauthFilterChain<JwtFilterChain> {

    private final JwtUtil jwtUtil;

    private final UsernamePasswordAuthenticationManager authenticationManager;

    public JwtFilterChain(HttpSecurity http, JwtUtil jwtUtil,
        UsernamePasswordAuthenticationManager authenticationManager) throws Exception {

        super(http);
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;

        super.setHttpBasicDisable();
        super.setSessionStateless();
        super.setFormLoginDisable();
        super.setCsrfDisable();
    }

    @Override
    public JwtFilterChain cors(CorsConfigurationSource source)
        throws Exception {
        this.http.cors(corsConfigurer -> corsConfigurer.configurationSource(source));
        return this;
    }

    public JwtFilterChain authorizeHttpRequests(
        Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> authorizeHttpRequestsCustomizer)
        throws Exception {

        this.http.authorizeHttpRequests(authorizeHttpRequestsCustomizer);

        return this;
    }
//    TODO 로그인 방식 선택 (username + password, oauth, rememberMe)

    public JwtFilterChain UsernamePassword() {
        return UsernamePassword("login");
    }

    public JwtFilterChain UsernamePassword(String pattern) {
        super.addFilterBefore(new JwtUsernamePasswordAuthenticationFilter("/"+pattern,
            this.jwtUtil, this.authenticationManager), RequestCacheAwareFilter.class);

        return this;
    }

    @Override
    public SecurityFilterChain build() throws Exception { return super.http.build(); }

    public HttpSecurity createConfiguredHttpSecurity() throws Exception { return super.http; }
}
