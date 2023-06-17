package org.swmaestro.kauth.oauth.info.provider;

import org.swmaestro.kauth.oauth.info.OAuth2UserInfo;

import java.util.Map;

/**
 * 네이버 OAuth2 로그인을 통해 얻을 수 있는 유저 정보 구현체
 * @author pyo
 */
public class NaverOAuth2UserInfo extends OAuth2UserInfo {

    Map<String, Object> response;

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
        response = (Map<String, Object>) attributes.get("response");
    }

    /**
     * 네이버에 등록된 사용자 회원 번호 반환
     * @return (String) "12345678"
     */
    @Override
    public String getId() {
        if (response == null) {
            return null;
        }

        return (String) response.get("id");
    }

    /**
     * 사용자 실제 이름 반환
     * @return (String) "홍길동"
     */
    @Override
    public String getName() {
        if (response == null) {
            return null;
        }

        return (String) response.get("name");
    }

    /**
     * 사용자가 네이버에 등록한 이메일 반환
     * @return (String) "example@ex.com"
     */
    @Override
    public String getEmail() {
        if (response == null) {
            return null;
        }

        return (String) response.get("email");
    }

    /**
     * 사용자 프로필 사진 URL 반환
     * @return (String) "https://ssl.pstatic.net/.../nodata_33x33.gif"
     */
    @Override
    public String getImageUrl() {
        if (response == null) {
            return null;
        }

        return (String) response.get("profile_image");
    }

    /**
     * 사용자가 등록한 네이버 닉네임 반환
     * 닉네임을 등록하지 않았으면 "id***" 형태로 반환
     * @return (String) "길동이"
     */
    public String getNickname() {
        if (response == null) {
            return null;
        }

        return (String) response.get("nickname");
    }


    /**
     * 사용자 연령대를 반환
     * 20대 -> "20-29"
     * @return (String) "20-29"
     */
    public String getAgeRange() {
        if (response == null) {
            return null;
        }

        return (String) response.get("age");
    }

    /**
     * 성별 반환
     * F(여자), M(남자), U(확인불가)
     * @return (String) "F"
     */
    public String getGender() {
        if (response == null) {
            return null;
        }

        return (String) response.get("gender");
    }

    /**
     * 사용자 생일을 "MM-DD" 형태로 반환
     * @return (String) "10-01"
     */
    public String getBirthday() {
        if (response == null) {
            return null;
        }

        return (String) response.get("birthday");
    }

    /**
     * 출생 연도를 "YYYY" 형태로 반환
     * @return (String) "1999"
     */
    public String getBirthyear() {
        if (response == null) {
            return null;
        }

        return (String) response.get("birthyear");
    }

    /**
     * 사용자 휴대폰 번호 반환
     * @return (String) "010-1234-5678"
     */
    public String getPhoneNumber() {
        if (response == null) {
            return null;
        }

        return (String) response.get("mobile");
    }
}
