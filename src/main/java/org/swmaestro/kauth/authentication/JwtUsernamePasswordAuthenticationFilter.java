package org.swmaestro.kauth.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.swmaestro.kauth.util.JwtUtil;
import org.swmaestro.kauth.util.KauthBeansProvider;

/**
 * 인증/인가 관리 전략 JWT,
 * 로그인 방식 username/password
 * 인증 필터
 * @author ChangEn Yea
 */
public class JwtUsernamePasswordAuthenticationFilter extends
    AbstractUsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil = KauthBeansProvider.getJwtUtil();

    public JwtUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, String pattern) {
        super(authenticationManager, new AntPathRequestMatcher(pattern,"POST"));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        UserDetails user = (UserDetails) authResult;

//        토큰 발급
        String accessToken = jwtUtil.createAccessToken(user.getUsername());
        String refreshToken = jwtUtil.createRefreshToken(user.getUsername());

//        TODO: RefreshToken 저장

//        TODO: HttpServletResponse에 토큰 담아서 response
    }
}
