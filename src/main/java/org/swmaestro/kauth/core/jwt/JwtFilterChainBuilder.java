package org.swmaestro.kauth.core.jwt;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.swmaestro.kauth.core.KauthFilterChain;
import org.swmaestro.kauth.core.KauthFilterChainBuilder;
import org.swmaestro.kauth.util.JwtUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JWT로 인증/인가를 처리하는 Kauth의 필터체인 구성을 위한 {@link KauthFilterChain}를 생성한다.
 * @author ChangEn Yea
 */
public class JwtFilterChainBuilder extends KauthFilterChainBuilder<JwtFilterChain> {

	private final JwtUtil jwtUtil;

	private final ObjectMapper objectMapper;

	/**
	 * 인스턴스를 생성한다.
	 * @param httpSecurity          {@link HttpSecurity}
	 * @param jwtUtil               {@link JwtUtil}
	 * @param authenticationManager {@link JwtUtil}
	 * @param objectMapper {@link ObjectMapper}
	 */
	public JwtFilterChainBuilder(HttpSecurity httpSecurity, JwtUtil jwtUtil,
		AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
		super(httpSecurity, authenticationManager);
		this.jwtUtil = jwtUtil;
		this.objectMapper = objectMapper;
	}

	/**
	 * {@link JwtFilterChain} 인스턴스를 생성하고 반환한다.
	 * @return {@link JwtFilterChain} 인스턴스
	 * @throws Exception
	 */
	@Override
	public JwtFilterChain init() throws Exception {
		return new JwtFilterChain(httpSecurity, jwtUtil, authenticationManager, objectMapper);
	}
}
