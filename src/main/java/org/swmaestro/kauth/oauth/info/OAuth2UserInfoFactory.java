package org.swmaestro.kauth.oauth.info;

import org.springframework.stereotype.Component;
import org.swmaestro.kauth.oauth.info.provider.ProviderType;
import org.swmaestro.kauth.oauth.info.provider.GoogleOAuth2UserInfo;
import org.swmaestro.kauth.oauth.info.provider.KakaoOAuth2UserInfo;
import org.swmaestro.kauth.oauth.info.provider.NaverOAuth2UserInfo;

import java.util.Map;

/**
 * OAuth Provider 별로 사용자 정보를 반환하는 Factory Class
 * @author pyo
 */
@Component
public class OAuth2UserInfoFactory {
    public OAuth2UserInfo getOAuth2UserInfo(ProviderType providerType, Map<String, Object> attributes) {
        switch (providerType) {
            case GOOGLE: return new GoogleOAuth2UserInfo(attributes);
            case NAVER: return new NaverOAuth2UserInfo(attributes);
            case KAKAO: return new KakaoOAuth2UserInfo(attributes);
            default: throw new IllegalArgumentException("Unsupported Provider Type");
        }
    }
}
