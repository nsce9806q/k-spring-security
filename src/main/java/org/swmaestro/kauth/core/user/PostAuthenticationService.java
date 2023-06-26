package org.swmaestro.kauth.core.user;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * 인증 이후 처리를 위한 인터페이스
 * @author ChangEn Yea
 */
public interface PostAuthenticationService {

	/**
	 * 비밀번호 오류시 호출되는 메서드
	 * @param username
	 * @return 비밀번호 틀린 횟수. (-1 이면 틀린 횟수를 알려주지 않는다)
	 */
	default Integer handleBadCredentialsException(String username) {
		return -1;
	}

	/**
	 * 인증 성공 후 호출되는 추가적인 처리를 위한 메서드
	 * @param userDetails {@link UserDetails}
	 */
	default void handleSuccessfulAuthentication(UserDetails userDetails) {

	}
}
