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

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JWT를 발급하는 username + password 인증 필터
 * @author ChangEn Yea
 */

public class JwtUsernamePasswordAuthenticationFilter extends
	AbstractUsernamePasswordAuthenticationFilter {

	private final JwtUtil jwtUtil;

	/**
	 * 인스턴스를 생성한다.
	 * @param pattern URI 패턴
	 * @param jwtUtil {@link JwtUtil}
	 * @param authenticationManager {@link AuthenticationManager}
	 * @param objectMapper {@link ObjectMapper}
	 */
	public JwtUsernamePasswordAuthenticationFilter(String pattern, JwtUtil jwtUtil,
		AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
		super(new AntPathRequestMatcher(pattern, "POST"), authenticationManager, objectMapper);
		this.jwtUtil = jwtUtil;
	}

	/**
	 * {@link #attemptAuthentication}에서 인증 성공시 토큰을 발급한다.
	 * Refresh토큰은 서버에 저장하고 토큰을 담아 200 응답을 반환하도록 설정한다.
	 * @param request {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @param chain {@link FilterChain}
	 * @param authResult {@link Authentication}
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request,
		HttpServletResponse response, FilterChain chain, Authentication authResult)
		throws IOException, ServletException {

		UserDetails user = (UserDetails)authResult;

		String accessToken = jwtUtil.createAccessToken(user.getUsername());
		String refreshToken = jwtUtil.createRefreshToken(user.getUsername());

		// TODO: RefreshToken 저장

		// TODO: HttpServletResponse에 토큰 담아서 response
	}
}
