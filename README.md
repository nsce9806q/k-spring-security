# k-spring-security

**Spring Security** 기반의 **REST API** 환경에서의 
**한국형 인증/인가 시나리오** 시큐리티 필터 체인을 구성을 돕는 오픈 소스 라이브러리

## 프로젝트 소개

- 여러 **클라이언트**(안드로이드, iOS, React)와 MSA 등의 다양해진 환경으로 인해 Spring Framework는 **REST API 서버**로 많이 활용되고 있음
- Spring Security에서는 기본적으로 인증/인가 필터를 제공하지만, 실질적인 요구사항 (네이버/카카오 소셜로그인, JWT 등)을 만족시키기에는 **개발자가 직접 구현하고 커스텀 필터를 만들어 적용**해야 한다
- 빠른 MVP를 출시와 같은 상황에서 로그인 기능이 필요할 때, Spring Security를 적용하여 커스텀 하기에는 많은 리소스가 요구됨
- **한국에서 자주 사용되는 인증/인가 시나리오**를 분석하여 이를 토대로 인증/인가 방식과 (JWT/세션) 로그인 방식 (네이버/카카오/구글 소셜 로그인, 아이디+비밀번호 로그인) 제공
- 라이브러리 사용자가 **최소한의 설정과 구현**으로 인증/인가 기능을 제공하면서 필요시 **확장에도 열려있는 설계**를 하는 것이 목표

## 사용 예시

```java
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	@Bean
	public SecurityFilterChain filterChain(JwtFilterChainBuilder builder)
		throws Exception {
	
		return builder.init()
			.usernamePassword().pattern("login").and()
			.oauth2Login().naver().kakao().and()
			.cors(new customCorsConfiguration())
			.authorizeHttpRequests(authorizeRequestMatcher -> authorizeRequestMatcher
				.requestMatchers("/user/**").hasRole("USER")
				.anyRequest().permitAll())
			.build()
	}
}
```

- 기존의 SecurityFilterChain을 구성하기 위해 Bean으로 등록하는 메소드의 파라미터를 **HttpSecurity를 의존성 주입 받는 대신**, 본 라이브러리에서 제공하는 인증/인가 방식의 필터 체인 빌더 (JwtFilterChainBuilder 또는 SessionFilterChainBuilder)를 주입 받아 **시큐리티 필터 체인 구성**
- 필터 체인 빌더
    - 로그인 방식 선택 가능
    - Cors 설정 등록 가능
    - uri 패턴 별 권한 설정 가능
    - 필요 시 빌더 메소드 체이닝을 통해 HttpSecurity 인스턴스 받아 커스텀 가능


### 인증/인가 방식

- JWT
- 세션

### 로그인 방식

- Username + Password
- Oauth 2.0
    - 네이버
    - 카카오
    - 구글
    - 애플 (예정)
- RememberMe (예정)

## Guides

추후 작성 예정

## Contributing

[CONTRIBUTING.md](./CONTRIBUTING.md)를 참고해주세요.

## Contributors

추후 작성 예정
