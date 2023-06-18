package org.swmaestro.kauth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.swmaestro.kauth.dto.UsernamePasswordLoginRequest;

/**
 * Kauth username + password 인증 추상 클래스 필터
 * @author ChangEn Yea
 */
public abstract class AbstractUsernamePasswordAuthenticationFilter extends AbstractAuthenticationFilter {

	private final UsernamePasswordAuthenticationManager authenticationManager;

	private final ObjectMapper objectMapper;

	/**
	 * 인스턴스를 생성한다.
	 * @param antPathRequestMatcher {@link AntPathRequestMatcher}
	 * @param authenticationManager {@link AuthenticationManager}
	 * @param objectMapper {@link ObjectMapper}
	 */
	protected AbstractUsernamePasswordAuthenticationFilter(AntPathRequestMatcher antPathRequestMatcher,
		AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
		super(antPathRequestMatcher);
		this.authenticationManager = (UsernamePasswordAuthenticationManager)authenticationManager;
		this.objectMapper = objectMapper;
		this.authenticationManager.lazyLoadDependency();
	}

	/**
	 * {@link HttpServletRequest}의 body에 있는 username과 password를
	 * {@link UsernamePasswordAuthenticationManager}를 통해 인증한다.
	 * @param request {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @return 인증 된 {@link Authentication}
	 * @throws AuthenticationException 로그인 오류(아이디, 비밀번호 오류 등), 계정 잠김 등 인증 실패 시
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
		throws AuthenticationException {
		try {
			UsernamePasswordLoginRequest loginRequest = objectMapper
				.readValue(request.getInputStream(), UsernamePasswordLoginRequest.class);

			return this.authenticationManager.authenticate(new AuthenticationProvider(
				loginRequest.getUsername(), loginRequest.getPassword()));

		} catch (IOException e) {
			super.logger.error(e);
		}
		return null;
	}

	/**
	 * TODO
	 * {@link #attemptAuthentication}에서 인증 실패시 각 상황에 맞게 로직을 처리하고 401 응답을 반환하도록 설정한다..
	 * @param request {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @param failed {@link AuthenticationException}
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException failed) throws IOException, ServletException {

		System.out.println("login failed");
		// 각 상황에 맞게 로직을 처리하고 401 응답을 반환하도록 설정한다.
	}

}
