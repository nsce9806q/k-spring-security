package org.swmaestro.kauth.core;

import jakarta.servlet.Filter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;
import org.swmaestro.kauth.core.jwt.JwtFilterChain;

/**
 * Kauth 설정 시작 점, 인증/인가 전략 선택
 * @author ChangEn Yea
 */
public abstract class KauthFilterChain<T> {

    protected final HttpSecurity http;

    public KauthFilterChain(HttpSecurity http) {
        this.http = http;
    }

    protected void setSessionStateless() throws Exception {
        this.http.sessionManagement(sessionManagementConfigurer ->
            sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    }

    protected void setHttpBasicDisable() throws Exception {
        this.http.httpBasic(AbstractHttpConfigurer::disable);
    }

    protected void setFormLoginDisable() throws Exception {
        this.http.formLogin(AbstractHttpConfigurer::disable);
    }

    protected void setCsrfDisable() throws Exception {
        this.http.csrf(AbstractHttpConfigurer::disable);
    }

    public abstract SecurityFilterChain build() throws Exception;

    protected void addFilterBefore(Filter filter,  Class<? extends Filter> beforeFilter) {
        this.http.addFilterBefore(filter, beforeFilter);
    }

    public abstract T cors(CorsConfigurationSource source) throws Exception;

    public abstract JwtFilterChain authorizeHttpRequests(
        Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> authorizeHttpRequestsCustomizer)
        throws Exception;
}
