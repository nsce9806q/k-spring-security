package org.swmaestro.kauth.oauth.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * UserPrincipal 에 도메인 모델을 주입해 도메인 모델 접근이 용이하도록 하는 추상 클래스
 * 추상 클래스 KauthUserPrincipal 을 상속하고 사용자가 정의한 도메인 모델에 맞게 메소드를 재정의
 * <M> : 유저(멤버) 도메인 모델
 * @author pyo
 * @param <M>
 */
public abstract class KauthUserPrincipal<M> implements OAuth2User, UserDetails {

    private M member;
    private Map<String, Object> attributes;

    /**
     * 일반 로그인 했을 때의 UserPrincipal
     * @param member
     */
    public KauthUserPrincipal(M member) {
        this.member = member;
    }

    /**
     * 소셜 로그인 했을 때의 UserPrincipal
     * @param member
     * @param attributes
     */
    public KauthUserPrincipal(M member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }
}
