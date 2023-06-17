package org.swmaestro.kauth.oauth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.swmaestro.kauth.oauth.entity.KauthUserPrincipal;
import org.swmaestro.kauth.oauth.info.provider.ProviderType;
import org.swmaestro.kauth.oauth.info.OAuth2UserInfo;
import org.swmaestro.kauth.oauth.info.OAuth2UserInfoFactory;

/**
 * Provider 로부터 받은 OAuth2 유저(User) 정보를 기존 계정(Member)과 연계하여 처리(회원가입, 정보 갱신 등)하는 서비스
 * 사용자는 이 클래스를 상속하는 Custom 서비스 클래스를 만들고
 * processMemberByOAuth2UserInfo 메소드에 기존 계정(사용자가 정의한 멤버 도메인 모델)과 연계해서 처리하는 기능을 구현한다.
 * @author pyo
 */
@RequiredArgsConstructor
public abstract class KOAuth2UserService extends DefaultOAuth2UserService {

    private final OAuth2UserInfoFactory oAuth2UserInfoFactory;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = makeOAuth2UserInfoByProvider(userRequest, user);

        return processMemberByOAuth2UserInfo(oAuth2UserInfo);
    }

    /**
     * 로그인한 Provider 가 무엇인지 판별하고 그에 맞는 OAuth2UserInfo 를
     * processMemberByOAuth2UserInfo 에 넘겨주는 메소드
     * @param userRequest
     * @param user
     * @return (OAuth2UserInfo)
     */
    private OAuth2UserInfo makeOAuth2UserInfoByProvider(OAuth2UserRequest userRequest, OAuth2User user) {
        ProviderType providerType = ProviderType.valueOf(
                userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        return oAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
    }

    /**
     * OAuth2UserInfo 에서 얻은 정보를 바탕으로 기존 계정(Member)과 연계하여 처리(회원가입, 정보 갱신 등)하는 메소드
     * return 예시: return new KauthUserPrincipal(member, oAuth2UserInfo.getAttributes());
     * @param oAuth2UserInfo
     * @return (KauthUserPrincipal) 사용자가 정의한 멤버 모메인 모델과 OAuth2UserInfo의 attributes을 주입한 객체 반환
     */
    public abstract KauthUserPrincipal processMemberByOAuth2UserInfo(OAuth2UserInfo oAuth2UserInfo);
}
