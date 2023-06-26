package org.swmaestro.kauth.exceptions;

import org.swmaestro.kauth.core.user.RefreshTokenService;

/**
 * {@link RefreshTokenService#getRefreshToken}가 미구현 상태에서 호출이 될 경우에 발생하는 예외
 * @author ChangEn Yea
 */
public class RefreshTokenServiceUnavailableException extends RuntimeException{

	public RefreshTokenServiceUnavailableException(String message) {
		super(message, null);
	}
}
