package org.swmaestro.kauth.exceptions;

/**
 * {@link jakarta.servlet.http.HttpServletRequest} Refresh-Token 헤더가 없을 경우에 발생하는 예외
 * @author ChangEn Yea
 */
public class RefreshTokenMissingException extends RuntimeException {

	public RefreshTokenMissingException() {
		this("RefreshToken is missing", null);
	}

	public RefreshTokenMissingException(String message, Throwable cause) {
		super(message, cause);
	}
}
