package org.swmaestro.kauth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.swmaestro.kauth.dto.UsernamePasswordLoginRequest;
import org.swmaestro.kauth.util.KauthBeansProvider;

/**
 * username + password 인증 필터
 * @author ChangEn Yea
 */
public abstract class AbstractUsernamePasswordAuthenticationFilter extends AbstractAuthenticationFilter {

    private final UsernamePasswordAuthenticationManager authenticationManager;

    protected AbstractUsernamePasswordAuthenticationFilter(AntPathRequestMatcher antPathRequestMatcher,
        UsernamePasswordAuthenticationManager authenticationManager) {
        super(antPathRequestMatcher);
        this.authenticationManager = authenticationManager;
        this.authenticationManager.lazyLoadDependency();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException, IOException, ServletException {
            try {
                ObjectMapper objectMapper = KauthBeansProvider.getObjectMapper();

                UsernamePasswordLoginRequest loginRequest = objectMapper
                    .readValue(request.getInputStream(), UsernamePasswordLoginRequest.class);

                return this.authenticationManager.authenticate(new AuthenticationProvider(
                    loginRequest.getUsername(), loginRequest.getPassword()));

            } catch (IOException e) {
                super.logger.error(e);
            }
            return null;
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException failed) throws IOException, ServletException {

        System.out.println("login failed");
//        TODO username + password 인증 실패 시 처리 (계정 잠금 등)
    }


}
