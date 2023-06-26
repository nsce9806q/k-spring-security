package org.swmaestro.kauth.core.user;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Kauth로 {@link org.springframework.security.web.SecurityFilterChain} 구성시 구현이 필요한 인터페이스
 * @author ChangEn Yea
 */
public interface KauthUserDetailsService extends UserDetailsService, PostAuthenticationService {
}
