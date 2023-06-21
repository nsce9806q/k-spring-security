package org.swmaestro.kauth.authentication;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;

/**
 * Kauth에서 사용하는 {@link Authentication} 구현 클래스
 * @author ChangEn Yea
 */
public class AuthenticationProvider implements Authentication, CredentialsContainer {

	private final Object principal;

	private Object credential;

	private Object details;

	private Collection<? extends GrantedAuthority> authorities;

	private boolean isAuthenticated;

	/**
	 * principal과 권한으로 인스턴스를 생성한다.
	 * @param principal {@link #principal}
	 * @param details {@link #details}
	 * @param authorities {@link Collection<GrantedAuthority>}
	 */
	public AuthenticationProvider(Object principal, Object details,
		Collection<? extends GrantedAuthority> authorities) {
		this.principal = principal;
		this.details = details;
		this.authorities = authorities;
	}

	/**
	 * principal과 credential로 인스턴스를 생성한다.
	 * @param principal {@link Object}
	 * @param credential {@link Object}
	 */
	public AuthenticationProvider(Object principal, Object credential) {
		this.principal = principal;
		this.credential = credential;
	}

	/**
	 * 권한을 설정한다.
	 * @param authorities {@link Collection<GrantedAuthority>}
	 */
	public void setAuthorities(
		Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	/**
	 * 권한을 반환한다.
	 * @return 이 {@link Authentication}의 권한
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	/**
	 * {@link #credential}을 반환한다.
	 * @return 이 {@link Authentication}의 {@link #credential}
	 */
	@Override
	public Object getCredentials() {
		return this.credential;
	}

	/**
	 * {@link #details}을 반환한다.
	 * @return {@link Authentication}의 {@link #details}
	 */
	@Override
	public Object getDetails() {
		return this.details;
	}

	/**
	 * {@link #principal}을 반환한다.
	 * @return {@link Authentication}의 {@link #principal}
	 */
	@Override
	public Object getPrincipal() {
		return this.principal;
	}

	/**
	 * 인증 여부를 반환한다.
	 * @return {@link Authentication}의 인증 여부
	 */
	@Override
	public boolean isAuthenticated() {
		return this.isAuthenticated;
	}

	/**
	 * {@link #details}을 설정한다.
	 * @param details {@link org.springframework.security.core.userdetails.UserDetails}
	 */
	public void setDetails(Object details) {
		this.details = details;
	}

	/**
	 * 이 인증 객체 인스턴스의 인증 여부를 설정한다.
	 * @param isAuthenticated <code>true</code> if the token should be trusted (which may
	 * result in an exception) or <code>false</code> if the token should not be trusted
	 * @throws IllegalArgumentException
	 */
	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		this.isAuthenticated = isAuthenticated;
	}

	@Override
	public String getName() {
		return null;
	}

	/**
	 * {@link #credential}을 삭제한다.
	 */
	@Override
	public void eraseCredentials() {
		this.credential = null;
	}
}
