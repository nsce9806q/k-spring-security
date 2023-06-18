package org.swmaestro.kauth.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.swmaestro.kauth.authentication.UsernamePasswordAuthenticationManager;
import org.swmaestro.kauth.core.jwt.JwtFilterChainBuilder;
import org.swmaestro.kauth.util.JwtUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Kauth 클래스 Bean 등록을 위한 설정
 * @author ChangEn Yea
 */
@Configuration
public class KauthConfiguration {

	/**
	 * {@link JwtFilterChainBuilder} Bean 등록
	 * @param httpSecurity {@link HttpSecurity}
	 * @param jwtUtil {@link JwtUtil}
	 * @param authenticationManager {@link UsernamePasswordAuthenticationManager}
	 * @param objectMapper {@link UsernamePasswordAuthenticationManager}
	 * @return {@link JwtFilterChainBuilder} 인스턴스
	 */
	@Bean
	public JwtFilterChainBuilder jwtFilterChainBuilder(HttpSecurity httpSecurity, JwtUtil jwtUtil,
		UsernamePasswordAuthenticationManager authenticationManager, ObjectMapper objectMapper) {
		return new JwtFilterChainBuilder(httpSecurity, jwtUtil, authenticationManager, objectMapper);
	}
}
