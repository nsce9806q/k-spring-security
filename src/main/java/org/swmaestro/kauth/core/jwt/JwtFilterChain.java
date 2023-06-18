package org.swmaestro.kauth.core.jwt;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.swmaestro.kauth.authentication.JwtUsernamePasswordAuthenticationFilter;
import org.swmaestro.kauth.core.KauthFilterChain;
import org.swmaestro.kauth.util.JwtUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JWT로 인증/인가를 처리하는 Kauth의 필터체인을 구성한다.
 * @author ChangEn Yea
 */
public class JwtFilterChain extends KauthFilterChain<JwtFilterChain> {

	private final JwtUtil jwtUtil;

	private final AuthenticationManager authenticationManager;

	private final ObjectMapper objectMapper;

	/**
	 * {@link JwtFilterChainBuilder#init()} 호출을 통해 인스턴스를 생성한다.
	 *
	 * @param http                  {@link HttpSecurity}
	 * @param jwtUtil               {@link JwtUtil}
	 * @param authenticationManager {@link AuthenticationManager}
	 * @param objectMapper {@link ObjectMapper}
	 * @throws Exception
	 */
	protected JwtFilterChain(HttpSecurity http, JwtUtil jwtUtil,
		AuthenticationManager authenticationManager, ObjectMapper objectMapper) throws Exception {

		super(http);
		this.jwtUtil = jwtUtil;
		this.authenticationManager = authenticationManager;
		this.objectMapper = objectMapper;

		super.setHttpBasicDisable();
		super.setSessionStateless();
		super.setFormLoginDisable();
		super.setCsrfDisable();
	}

	/**
	 * CORS 정책을 설정한다.
	 * @param source {@link CorsConfigurationSource}
	 * @return 현재 인스턴스
	 * @throws Exception
	 */
	@Override
	public JwtFilterChain cors(CorsConfigurationSource source)
		throws Exception {
		super.http.cors(corsConfigurer -> corsConfigurer.configurationSource(source));
		return this;
	}

	/**
	 * 현재 인스턴스의 {@link HttpSecurity}를 반환하고 {@link KauthFilterChain} 설정을 종료한다.
	 * @return 현재 인스턴스의 {@link HttpSecurity}
	 * @throws Exception
	 */
	@Override
	public JwtFilterChain authorizeHttpRequests(
		Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> authorizeHttpRequestsCustomizer)
		throws Exception {

		super.http.authorizeHttpRequests(authorizeHttpRequestsCustomizer);

		return this;
	}

	/**
	 * UsernamePassword 로그인 방식을 활성화 한다. (default endpoint uri: /login)
	 * @return 현재 인스턴스
	 */
	public JwtFilterChain UsernamePassword() {
		return UsernamePassword("login");
	}

	/**
	 * UsernamePassword 로그인 방식을 활성화 한다.
	 * @param pattern 로그인 요청 endpoint uri
	 * @return 현재 인스턴스
	 */
	public JwtFilterChain UsernamePassword(String pattern) {
		super.addFilterBefore(new JwtUsernamePasswordAuthenticationFilter("/" + pattern,
			this.jwtUtil, this.authenticationManager, this.objectMapper), RequestCacheAwareFilter.class);

		return this;
	}
}
