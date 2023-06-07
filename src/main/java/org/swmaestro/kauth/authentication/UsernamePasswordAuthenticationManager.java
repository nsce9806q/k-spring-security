package org.swmaestro.kauth.authentication;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.swmaestro.kauth.util.KauthBeansProvider;

@Component
public class UsernamePasswordAuthenticationManager implements AuthenticationManager {

    private UserDetailsService userDetailsService = null;

    private PasswordEncoder passwordEncoder = null;

    protected void lazyLoadDependency() {
        if(this.userDetailsService == null) {
            this.userDetailsService = KauthBeansProvider.getUserDetailsService();
        }

        if(this.passwordEncoder == null) {
            this.passwordEncoder = KauthBeansProvider.getPasswordEncoder();
        }
    }

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {

        AuthenticationProvider auth = (AuthenticationProvider) authentication;
        UserDetails user = userDetailsService.loadUserByUsername((String) auth.getPrincipal());



        if(passwordEncoder.matches((String) auth.getCredentials(), user.getPassword())) {
            auth.setAuthenticated(true);
            auth.eraseCredentials();
            auth.setAuthorities(user.getAuthorities());

            return auth;
        }

        return null;
    }
}
