package org.swmaestro.kauth.authentication.jwt;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.swmaestro.kauth.core.user.RefreshTokenService;
import org.swmaestro.kauth.exceptions.RefreshTokenServiceUnavailableException;

/**
 * RefreshToken을 get/set 한다.
 * @author ChangEn Yea
 */
@Lazy
@Component
public class RefreshTokenManager {

	private final RefreshTokenService refreshTokenService;

	/**
	 * 인스턴스를 생성한다.
	 * @param refreshTokenService {@link RefreshTokenService}
	 */
	public RefreshTokenManager(RefreshTokenService refreshTokenService) {
		this.refreshTokenService = refreshTokenService;
	}

	/**
	 * {@link RefreshTokenService#getRefreshToken}를 통해 RefreshToken을 가져온다.
	 * @param username
	 * @return RefreshToken
	 * @throws RefreshTokenServiceUnavailableException {@link RefreshTokenService#getRefreshToken} 메소드가
	 * null을 반환 할 때 (구현이 안되어 있을 때) 발생
	 */
	String getRefreshToken(String username) throws RefreshTokenServiceUnavailableException {
		String refreshToken = refreshTokenService.getRefreshToken(username);

		if (refreshToken == null) {
			throw new RefreshTokenServiceUnavailableException(
				"RefreshTokenService.getRefreshToken(String) is not defined");
		}
		return refreshToken;
	}

	/**
	 * {@link RefreshTokenService#setRefreshToken}를 통해 RefreshToken을 저장한다.
	 * @param refreshToken
	 * @param username
	 */
	void setRefreshToken(String refreshToken, String username) {
		refreshTokenService.setRefreshToken(refreshToken, username);
	}
}
