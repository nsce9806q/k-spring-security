package org.swmaestro.kauth.core.user;

/**
 * JWT RefreshToken 전략 사용시 구현이 필요한 인터페이스
 * @author ChangEn Yea
 */
public interface RefreshTokenService {

	/**
	 * username으로 RefreshToken를 가져온다.
	 * @param username
	 * @return RefreshToken
	 */
	default String getRefreshToken(String username) { return null; }

	/**
	 * refreshToken을 서버에 저장한다.
	 * @param refreshToken
	 * @param username
	 */
	default void setRefreshToken(String refreshToken, String username) {}
}
