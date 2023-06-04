package org.swmaestro.kauth.core;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;
import org.swmaestro.kauth.authentication.JwtUsernamePasswordAuthenticationFilter;


/**
 * 인증/인가 전략: JWT
 * @author ChangEn Yea
 */
public class JwtConfigurer extends KauthConfigurer {

    private JwtConfigurer(HttpSecurity http) throws Exception {
        super.http = http;
        super.authenticationManager = http.getSharedObject(AuthenticationManager.class);
        super.setStatelessSession();
        super.setFormLoginDisable();
    }

    public static JwtConfigurer init(HttpSecurity http) throws Exception {
        return new JwtConfigurer(http);
    }

//    TODO 로그인 방식 선택 (username + password, oauth, rememberMe)
    public JwtConfigurer selectLoginProvider() {

        return this;
    }

    public void UsernamePassword(String pattern) {
        super.addFilterBefore(new JwtUsernamePasswordAuthenticationFilter(
            super.authenticationManager, pattern), RequestCacheAwareFilter.class);
    }

    @Override
    public HttpSecurity complete() {
        return super.http;
    }
}
