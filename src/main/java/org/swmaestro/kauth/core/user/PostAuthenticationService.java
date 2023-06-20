package org.swmaestro.kauth.core.user;

import org.springframework.security.core.userdetails.UserDetails;

public interface PostAuthenticationService {

	default Integer handleBadCredentialsException(String username) {
		return -1;
	}

	default void handleSuccessfulAuthentication(UserDetails userDetails) {

	}
}
