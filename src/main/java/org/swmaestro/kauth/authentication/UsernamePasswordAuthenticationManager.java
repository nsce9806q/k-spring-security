package org.swmaestro.kauth.authentication;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.swmaestro.kauth.util.KauthBeansProvider;

/**
 * Username + Password를 사용하는 {@link AuthenticationManager}
 * @author ChangEn Yea
 */
@Component
public class UsernamePasswordAuthenticationManager implements AuthenticationManager {

	private UserDetailsService userDetailsService = null;

	private PasswordEncoder passwordEncoder = null;

	/**
	 * {@link UserDetailsService}와 {@link PasswordEncoder} 구현 클래스 Bean을 주입한다.
	 */
	protected void lazyLoadDependency() {
		if (this.userDetailsService == null) {
			this.userDetailsService = KauthBeansProvider.getUserDetailsService();
		}

		if (this.passwordEncoder == null) {
			this.passwordEncoder = KauthBeansProvider.getPasswordEncoder();
		}
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

		if (passwordEncoder.matches((String)auth.getCredentials(), user.getPassword())) {
			auth.setAuthenticated(true);
			auth.eraseCredentials();
			auth.setAuthorities(user.getAuthorities());

			return auth;
		}

		// TODO throw AuthenticationException
		return null;
	}
}
