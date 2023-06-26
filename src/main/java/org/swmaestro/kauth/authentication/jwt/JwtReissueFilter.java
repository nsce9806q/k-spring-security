package org.swmaestro.kauth.authentication.jwt;

import java.io.IOException;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.swmaestro.kauth.exceptions.RefreshTokenMismatchException;
import org.swmaestro.kauth.exceptions.RefreshTokenMissingException;
import org.swmaestro.kauth.exceptions.RefreshTokenServiceUnavailableException;
import org.swmaestro.kauth.util.HttpServletResponseUtil;
import org.swmaestro.kauth.util.JwtUtil;

import com.auth0.jwt.exceptions.JWTVerificationException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 토큰 재발급 필터
 * @author ChangEn Yea
 */
public class JwtReissueFilter extends OncePerRequestFilter {

	private final AntPathRequestMatcher requestMatcher;

	private final JwtUtil jwtUtil;

	private final RefreshTokenManager refreshTokenManager;

	private final HttpServletResponseUtil responseUtil;

	/**
	 * 인스턴스를 생성한다.
	 * @param requestMatcher {@link AntPathRequestMatcher}
	 * @param jwtUtil {@link JwtUtil}
	 * @param refreshTokenManager {@link RefreshTokenManager}
	 * @param responseUtil {@link HttpServletResponseUtil}
	 */
	public JwtReissueFilter(AntPathRequestMatcher requestMatcher, JwtUtil jwtUtil,
		RefreshTokenManager refreshTokenManager, HttpServletResponseUtil responseUtil) {
		this.requestMatcher = requestMatcher;
		this.jwtUtil = jwtUtil;
		this.refreshTokenManager = refreshTokenManager;
		this.responseUtil = responseUtil;
	}

	/**
	 * 요청이 {@link #requestMatcher}와 일치하면 요청 Refresh-Token 헤더의 토큰을 검증한다.
	 * 헤더와 서버 내의 두 refreshToken을 비교하고,
	 * 같은 토큰이라면 새로운 토큰을 발급하고 저장하고 응답 헤더에 토큰을 설정한다.
	 * @param request {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @param filterChain {@link FilterChain}
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		if (requestMatcher.matches(request)) {
			try {
				String token = request.getHeader("Refresh-Token");
				if (token == null) {
					throw new RefreshTokenMissingException();
				}

				String username = jwtUtil.verifyToken(token);

				if (!refreshTokenManager.getRefreshToken(username).equals(token)) {
					responseUtil.setUnauthorizedResponse(response,
						new RefreshTokenMismatchException("RefreshTokens are not match."));
				}

				String accessToken = jwtUtil.createAccessToken(username);
				String refreshToken = jwtUtil.createRefreshToken(username);

				refreshTokenManager.setRefreshToken(refreshToken, username);

				responseUtil.setHeader(response, "Authorization", accessToken);
				responseUtil.setHeader(response, "Refresh-Token", refreshToken);

			} catch (RefreshTokenMissingException | RefreshTokenServiceUnavailableException |
					 RefreshTokenMismatchException | JWTVerificationException e) {
				responseUtil.setUnauthorizedResponse(response, e);
			}
		} else {
			filterChain.doFilter(request, response);
		}
	}
}
