package org.swmaestro.kauth.exceptions;

/**
 * 비교하는 두 RefreshToken이 불일치 시 발생하는 예외
 * @author ChangEn Yea
 */
public class RefreshTokenMismatchException extends RuntimeException{

	public RefreshTokenMismatchException(String message) {
		super(message, null);
	}
}
