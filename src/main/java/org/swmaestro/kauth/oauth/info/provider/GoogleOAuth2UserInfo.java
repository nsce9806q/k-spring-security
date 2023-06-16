package org.swmaestro.kauth.oauth.info.provider;

import org.swmaestro.kauth.oauth.info.OAuth2UserInfo;

import java.util.Map;

/**
 * 구글 OAuth2 로그인을 통해 얻을 수 있는 유저 정보 구현체
 * @author pyo
 */
public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    /**
     * 사용자 등록번호 반환
     * @return (String) "12345678"
     */
    @Override
    public String getId() {
        return (String) attributes.get("id");
    }

    /**
     * 사용자 이름 반환(실제 이름이 아닐 수 있음)
     * @return (String) "길동이"
     */
    @Override
    public String getName() {
        return (String) attributes.get("name");
    }


    /**
     * 등록된 사용자 이메일을 반환
     * @return (String) "example@ex.com"
     */
    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    /**
     * 사용자 프로필 이미지 URL 반환
     * 기본 프로필 이미지일 경우 이름 첫 글자로 된 랜덤 이미지 URL이 반환된다.
     * @return (String) "http://yyy.google.com/.../img_640x640.jpg"
     */
    @Override
    public String getImageUrl() {
        return (String) attributes.get("picture");
    }
}
