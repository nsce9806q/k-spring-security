package org.swmaestro.kauth.authentication;

import java.util.Collection;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;

public class AuthenticationProvider implements Authentication, CredentialsContainer {

    private final Object principal;
    private Collection<? extends GrantedAuthority> authorities;

    private Object credential;

    private boolean isAuthenticated;

    public AuthenticationProvider(Object principal,
        Collection<? extends GrantedAuthority> authorities) {
            this.principal = principal;
            this.authorities = authorities;
    }

    public AuthenticationProvider(Object principal, Object credential) {
        this.principal = principal;
        this.credential = credential;
    }

    public void setAuthorities(
        Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public Object getCredentials() {
        return this.credential;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public boolean isAuthenticated() {
        return this.isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void eraseCredentials() {
        this.credential = null;
    }
}
