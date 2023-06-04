package org.swmaestro.kauth.core;

import jakarta.servlet.Filter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * Kauth 설정 시작 점, 인증/인가 전략 선택
 * @author ChangEn Yea
 */
public abstract class KauthConfigurer {

    protected HttpSecurity http;

    protected AuthenticationManager authenticationManager;

    protected void setStatelessSession() throws Exception {
        this.http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    }

    protected void setFormLoginDisable() throws Exception {
        this.http.formLogin(AbstractHttpConfigurer::disable);
    }

    public abstract HttpSecurity complete();

    protected void addFilterBefore(Filter filter,  Class<? extends Filter> beforeFilter) {
        this.http.addFilterBefore(filter, beforeFilter);
    }

}
