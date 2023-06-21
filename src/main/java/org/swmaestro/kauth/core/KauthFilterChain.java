package org.swmaestro.kauth.core;

import jakarta.servlet.Filter;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;
import org.swmaestro.kauth.util.HttpServletResponseUtil;

/**
 * Kauth의 필터체인을 구성한다.
 * @param <T> 이 클래스의 상속 클래스
 * @author ChangEn Yea
 */
public abstract class KauthFilterChain<T> {

	protected final HttpSecurity http;

	protected final HttpServletResponseUtil responseUtil;

	/**
	 * {@link KauthFilterChainBuilder#init()} 호출을 통해 인스턴스를 생성한다.
	 *
	 * @param http         {@link HttpSecurity}
	 * @param responseUtil {@link HttpServletResponseUtil}
	 */
	protected KauthFilterChain(HttpSecurity http, HttpServletResponseUtil responseUtil) {
		this.http = http;
		this.responseUtil = responseUtil;
	}

	/**
	 * Spring Security가 Session을 생성하지도 않고, 사용하지도 않는 정책으로 설정한다.
	 * @throws Exception
	 */
	protected void setSessionStateless() throws Exception {
		this.http.sessionManagement(sessionManagementConfigurer ->
			sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	}

	/**
	 * Spring Security의 HTTP Basic 인증 비활성화한다.
	 * @throws Exception
	 */
	protected void setHttpBasicDisable() throws Exception {
		this.http.httpBasic(AbstractHttpConfigurer::disable);
	}

	/**
	 * Spring Security의 Form Login 비활성화한다.
	 * @throws Exception
	 */
	protected void setFormLoginDisable() throws Exception {
		this.http.formLogin(AbstractHttpConfigurer::disable);
	}

	/**
	 * Spring Security의 CSRF 비활성화한다.
	 * @throws Exception
	 */
	protected void setCsrfDisable() throws Exception {
		this.http.csrf(AbstractHttpConfigurer::disable);
	}

	/**
	 * CORS 정책을 설정한다.
	 * @param source {@link CorsConfigurationSource}
	 * @return 현재 인스턴스
	 * @throws Exception
	 */
	public abstract T cors(CorsConfigurationSource source) throws Exception;

	/**
	 * HTTP 요청 별 권한에 따른 인가 정책을 설정한다.
	 * @param authorizeHttpRequestsCustomizer {@link  AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry} 설정을 위한 {@link Customizer}
	 * @return 현재 인스턴스
	 * @throws Exception
	 */
	public abstract T authorizeHttpRequests(
		Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> authorizeHttpRequestsCustomizer)
		throws Exception;

	/**
	 * {@link Filter}를 beforeFilter 앞에 적용한다.
	 * @param filter 적용 할 {@link Filter}
	 * @param beforeFilter 적용 할 필터가 이 {@link Filter}보다 먼저 실행됨
	 */
	protected void addFilterBefore(Filter filter, Class<? extends Filter> beforeFilter) {
		this.http.addFilterBefore(filter, beforeFilter);
	}

	/**
	 * {@link SecurityFilterChain}을 반환하고 {@link KauthFilterChain} 설정을 종료한다.
	 * @return {@link SecurityFilterChain}
	 * @throws Exception
	 */
	public SecurityFilterChain build() throws Exception {
		return this.http.build();
	}

	/**
	 * 현재 인스턴스의 {@link HttpSecurity}를 반환하고 {@link KauthFilterChain} 설정을 종료한다.
	 * @return 현재 인스턴스의 {@link HttpSecurity}
	 * @throws Exception
	 */
	public HttpSecurity createConfiguredHttpSecurity() throws Exception {
		return this.http;
	}
}
