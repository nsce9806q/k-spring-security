package org.swmaestro.kauth.core;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.swmaestro.kauth.util.HttpServletResponseUtil;
import org.swmaestro.kauth.util.JwtUtil;

/**
 * Kauth의 필터체인 구성을 위한 {@link KauthFilterChain}를 생성한다.
 * @param <T> {@link KauthFilterChain}의 상속 클래스
 * @author ChangEn Yea
 */
public abstract class KauthFilterChainBuilder<T extends KauthFilterChain<?>> {

	protected final HttpSecurity httpSecurity;

	protected final AuthenticationManager authenticationManager;

	protected final HttpServletResponseUtil responseUtil;


	/**
	 * 인스턴스를 생성한다.
	 *
	 * @param httpSecurity          {@link HttpSecurity}
	 * @param authenticationManager {@link JwtUtil}
	 * @param responseUtil {@link HttpServletResponseUtil}
	 */
	protected KauthFilterChainBuilder(HttpSecurity httpSecurity,
		AuthenticationManager authenticationManager, HttpServletResponseUtil responseUtil) {
		this.httpSecurity = httpSecurity;
		this.authenticationManager = authenticationManager;
		this.responseUtil = responseUtil;
	}

	/**
	 * {@link KauthFilterChain}을 상속하는 클래스의 인스턴스를 생성하고 반환한다.
	 * @return {@link KauthFilterChain}을 상속하는 클래스의 인스턴스
	 * @throws Exception
	 */
	public abstract T init() throws Exception;
}
