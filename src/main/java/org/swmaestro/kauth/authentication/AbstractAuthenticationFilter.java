package org.swmaestro.kauth.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.swmaestro.kauth.util.HttpServletResponseUtil;

/**
 * Kauth 인증 추상 클래스 필터
 * @author ChangEn Yea
 */
public abstract class AbstractAuthenticationFilter extends OncePerRequestFilter {

	protected final Log logger = LogFactory.getLog(getClass());

	protected final AntPathRequestMatcher requestMatcher;

	protected final HttpServletResponseUtil responseUtil;

	/**
	 * 이 인증 필터의 {@link AntPathRequestMatcher}를 설정하고 인스턴스를 생성한다.
	 * @param requestMatcher {@link AntPathRequestMatcher}
	 * @param responseUtil {@link HttpServletResponseUtil}
	 */
	protected AbstractAuthenticationFilter(AntPathRequestMatcher requestMatcher, HttpServletResponseUtil responseUtil) {
		this.requestMatcher = requestMatcher;
		this.responseUtil = responseUtil;
	}

	/**
	 * 요청이 {@link #requestMatcher}와 일치하면 {@link #attemptAuthentication} 호출을 통해 인증하고
	 * 성공 여부에 따라 {@link #successfulAuthentication} 또는 {@link #unsuccessfulAuthentication}를 호출한다.
	 * @param request {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @param chain {@link FilterChain}
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
		throws IOException, ServletException {

		if (requestMatcher.matches(request)) {
			try {
				Authentication authenticationResult = attemptAuthentication(request, response);
				successfulAuthentication(request, response, chain, authenticationResult);
			} catch (InternalAuthenticationServiceException failed) {
				super.logger.error("An internal error occurred while trying to authenticate the user.", failed);
			} catch (AuthenticationException ex) {
				unsuccessfulAuthentication(request, response, ex);
			}
		} else {
			chain.doFilter(request, response);
		}
	}

	/**
	 * 사용자를 인증하고 인증 성공 시 {@link Authentication}를 반환한다.
	 * @param request {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @return 인증 된 {@link Authentication}
	 * @throws AuthenticationException 로그인 오류, 계정 잠김 등 인증 실패 시
	 * @throws IOException
	 * @throws ServletException
	 */
	public abstract Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
		throws AuthenticationException, IOException, ServletException;

	/**
	 * {@link #attemptAuthentication}에서 인증 성공시의 로직을 처리한다.
	 * @param request {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @param chain {@link FilterChain}
	 * @param authResult {@link Authentication}
	 * @throws IOException
	 * @throws ServletException
	 */
	protected abstract void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		FilterChain chain, Authentication authResult) throws IOException, ServletException;

	/**
	 * {@link #attemptAuthentication}에서 인증 실패시의 로직을 처리한다.
	 * @param request {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @throws IOException
	 * @throws ServletException
	 */
	protected abstract void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException failed) throws IOException, ServletException;

}
