package org.swmaestro.kauth.authentication;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.swmaestro.kauth.core.user.PostAuthenticationService;
import org.swmaestro.kauth.core.user.KauthUserDetailsService;

/**
 * Username + Password를 사용하는 {@link AuthenticationManager}
 * @author ChangEn Yea
 */
@Lazy
@Component
public class UsernamePasswordAuthenticationManager implements AuthenticationManager {

	private final KauthUserDetailsService userDetailsService;

	private final PasswordEncoder passwordEncoder;

	private final UserDetailsChecker userDetailsChecker = new AccountStatusUserDetailsChecker();

	/**
	 * 인스턴스를 생성한다.
	 * @param userDetailsService {@link KauthUserDetailsService}
	 * @param passwordEncoder {@link PasswordEncoder}
	 */
	public UsernamePasswordAuthenticationManager(KauthUserDetailsService userDetailsService,
		PasswordEncoder passwordEncoder) {
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * <pre>
	 * {@link UserDetailsService#loadUserByUsername}를 호출해서
	 * {@link Authentication}의 credentials과 {@link UserDetails}의 password를 {@link PasswordEncoder#matches}를 통해
	 * 일치 여부를 확인한다.
	 * 일치 한다면 {@link Authentication} 인스턴스를 인증 처리하고, credential를 삭제하고, 권한을 설정한다.
	 * </pre>
	 * @param authentication {@link Authentication}
	 * @return 인증 처리 된 {@link Authentication}
	 * @throws AuthenticationException
	 */
	@Override
	public Authentication authenticate(Authentication authentication)
		throws AuthenticationException {

		AuthenticationProvider auth = (AuthenticationProvider)authentication;
		UserDetails user = userDetailsService.loadUserByUsername((String)auth.getPrincipal());

		// AccountStatusException 체크
		userDetailsChecker.check(user);

		if (passwordEncoder.matches((String)auth.getCredentials(), user.getPassword())) {
			auth.setAuthenticated(true);
			auth.eraseCredentials();
			auth.setAuthorities(user.getAuthorities());
			userDetailsService.handleSuccessfulAuthentication(user);

			return auth;
		} else {
			throw new BadCredentialsException("Password does not matches.");
		}
	}

	/**
	 * {@link BadCredentialsException}을 처리하도록
	 * {@link PostAuthenticationService#handleBadCredentialsException}을 호출한다.
	 * @param username
	 * @return 비밀번호 틀린 횟수. (-1 이면 틀린 횟수를 알려주지 않는다)
	 */
	public Integer handleBadCredentialsException(String username) {
		return userDetailsService.handleBadCredentialsException(username);
	}
}
