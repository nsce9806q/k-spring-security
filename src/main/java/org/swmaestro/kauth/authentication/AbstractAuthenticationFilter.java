package org.swmaestro.kauth.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.GenericFilterBean;


/**
 * 기본 인증 필터
 * @author ChangEn Yea
 */
public abstract class AbstractAuthenticationFilter extends GenericFilterBean {

    protected final Log logger = LogFactory.getLog(getClass());

    protected final AuthenticationManager authenticationManager;

    protected final AntPathRequestMatcher requestMatcher;

    protected AbstractAuthenticationFilter(AuthenticationManager authenticationManager, AntPathRequestMatcher requestMatcher) {
        this.authenticationManager = authenticationManager;
        this.requestMatcher = requestMatcher;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        if(requestMatcher.matches((HttpServletRequest) request)) {
            doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
        } else {
            chain.doFilter(request, response);
        }

    }

    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        try {
            Authentication authenticationResult = attemptAuthentication(request, response);

            successfulAuthentication(request, response, chain, authenticationResult);
        } catch (InternalAuthenticationServiceException failed) {
            super.logger.error("An internal error occurred while trying to authenticate the user.", failed);
        } catch (AuthenticationException ex) {
            unsuccessfulAuthentication(request, response, ex);
        }

    }

    public abstract Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException, IOException, ServletException;

    protected abstract void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain, Authentication authResult) throws IOException, ServletException;

    protected abstract void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException failed) throws IOException, ServletException;


}
