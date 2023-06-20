package org.swmaestro.kauth.core.user;

/**
 * JWT RefreshToken 전략 사용시 구현이 필요한 인터페이스
 * @author ChangEn Yea
 */
public interface RefreshTokenService {
	default String getRefreshToken(String username) { return null; }

	default void setRefreshToken(String refreshToken, String username) {}
}
