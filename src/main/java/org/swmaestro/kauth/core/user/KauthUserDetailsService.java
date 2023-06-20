package org.swmaestro.kauth.core.user;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface KauthUserDetailsService extends UserDetailsService, PostAuthenticationService {

//	TODO 마지막 로그인 시간, 비밀번호 오류 횟수, 계정 잠금 등 설정하는 메소드
}
