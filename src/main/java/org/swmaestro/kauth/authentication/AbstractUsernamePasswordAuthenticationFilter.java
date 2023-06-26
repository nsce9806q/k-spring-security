package org.swmaestro.kauth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.swmaestro.kauth.dto.UsernamePasswordLoginRequest;
import org.swmaestro.kauth.util.HttpServletResponseUtil;

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
	 * @param responseUtil {@link HttpServletResponseUtil}
	 */
	protected AbstractUsernamePasswordAuthenticationFilter(AntPathRequestMatcher antPathRequestMatcher,
		AuthenticationManager authenticationManager, ObjectMapper objectMapper, HttpServletResponseUtil responseUtil) {
		super(antPathRequestMatcher, responseUtil);
		this.authenticationManager = (UsernamePasswordAuthenticationManager)authenticationManager;
		this.objectMapper = objectMapper;
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
			//	TODO 아이디 비번 잘못 들어왔다고 400 response 처리해야함
		}
		return null;
	}

	/**
	 * TODO 200 또는 401 선택
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

		// TODO 오류 메세지 형식
		if (failed.getClass().equals(UsernameNotFoundException.class)) {
			// UserDetailsService에서 UserDetails를 찾을 수 없는 경우, 아이디+비밀번호 오류 메세지
			super.responseUtil.setUnauthorizedResponse(response, failed);
		} else if (failed.getClass().equals(BadCredentialsException.class)) {
			Integer passwordFailureCount = authenticationManager.handleBadCredentialsException(
				objectMapper.readTree(request.getInputStream()).get("username").asText());

			if (passwordFailureCount == -1) {
				// 비밀번호 오류 횟수 비공개, 아이디+비밀번호 오류 메세지
				super.responseUtil.setUnauthorizedResponse(response, failed);
			} else {
				// 비밀번호 틀린 횟수 + 비밀번호 오류 메세지
				super.responseUtil.setUnauthorizedResponse(response, failed);
			}
		} else {
			// 기타 AuthenticationException 관련 메시지 (계정 잠금 등)
			super.responseUtil.setUnauthorizedResponse(response, failed);
		}
	}

}
