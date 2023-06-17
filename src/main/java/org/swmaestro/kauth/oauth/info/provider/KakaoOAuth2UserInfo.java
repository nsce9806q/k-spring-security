package org.swmaestro.kauth.oauth.info.provider;

import org.swmaestro.kauth.oauth.info.OAuth2UserInfo;

import java.util.Map;


/**
 * 카카오 OAuth2 로그인을 통해 얻을 수 있는 유저 정보 구현체
 * @author pyo
 */
public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    private Map<String, Object> kakaoAccount;
    private Map<String, Object> profile;

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
        this.kakaoAccount = (Map) attributes.get("kakao_account");
        this.profile = (Map) kakaoAccount.get("profile");
    }

    /**
     * 카카오에 등록된 회원번호 반환
     * @return (String) "q1w2e3r4"
     */
    @Override
    public String getId() {
        return (String) attributes.get("id");
    }

    /**
     * 카카오에 등록된 사용자의 실제 이름을 반환
     * @return (String) "홍길동"
     */
    @Override
    public String getName() {
        boolean nameNeedsAgreement = (boolean) kakaoAccount.get("name_needs_agreement");
        if (nameNeedsAgreement) {
            return (String) kakaoAccount.get("name");
        }
        return null;
    }

    /**
     * 카카오계정 이메일 반환
     * @return (String) "example@ex.com"
     */
    @Override
    public String getEmail() {
        boolean emailNeedsAgreement = (boolean) kakaoAccount.get("email_needs_agreement");
        if (emailNeedsAgreement) {
            boolean isEmailValid = (boolean) kakaoAccount.get("is_email_valid");
            boolean isEmailVerified = (boolean) kakaoAccount.get("is_email_verified");
            if (isEmailValid && isEmailVerified) {
                return (String) kakaoAccount.get("email");
            }
        }
        return null;
    }

    /**
     * 카카오 프로필 이미지 URL 반환
     * 기본 프로필 이미지일 경우 null 반환
     * @return (String) "http://yyy.kakao.com/dn/.../img_640x640.jpg"
     */
    @Override
    public String getImageUrl() {
        boolean profileImageNeedsAgreement = (boolean) kakaoAccount.get("profile_image_needs_agreement");
        if (profileImageNeedsAgreement) {
            if (!(boolean) profile.get("is_default_image")) {
                return (String) profile.get("profile_image_url");
            }
        }
        return null;
    }

    /**
     * 카카오 프로필 썸네일 이미지 URL 반환 (작은 크기)
     * 기본 프로필 이미지일 경우 null 반환
     * @return (String) "http://yyy.kakao.com/.../img_110x110.jpg"
     */
    public String getThumbnailImageUrl() {
        boolean profileImageNeedsAgreement = (boolean) kakaoAccount.get("profile_image_needs_agreement");
        if (profileImageNeedsAgreement) {
            if (!(boolean) profile.get("is_default_image")) {
                return (String) profile.get("thumbnail_image_url");
            }
        }
        return null;
    }

    /**
     * 사용자가 설정한 카카오 닉네임 반환
     * @return (String) "길동이"
     */
    public String getNickname() {
        boolean profileNicknameNeedsAgreement = (boolean) kakaoAccount.get("profile_nickname_needs_agreement");
        if (profileNicknameNeedsAgreement) {
            return (String) profile.get("nickname");
        }
        return null;
    }

    /**
     * 사용자의 연령대를 반환
     * 20대 -> "20~29"
     * @return (String) "20~29"
     */
    public String getAgeRange() {
        boolean ageRangeNeedsAgreement = (boolean) kakaoAccount.get("age_range_needs_agreement");
        if (ageRangeNeedsAgreement) {
            return (String) kakaoAccount.get("age_range");
        }
        return null;
    }

    /**
     * 생일을 "MMDD" 형태로 반환
     * @return (String) "1130"
     */
    public String getBirthday() {
        boolean birthdayNeedsAgreement = (boolean) kakaoAccount.get("birthday_needs_agreement");
        if (birthdayNeedsAgreement) {
            return (String) kakaoAccount.get("birthday");
        }
        return null;
    }

    /**
     * 생일 타입을 반환
     * SOLAR(양력) 또는 LUNAR(음력)
     * @return (String) "SOLAR"
     */
    public String getBirthdayType() {
        boolean birthdayNeedsAgreement = (boolean) kakaoAccount.get("birthday_needs_agreement");
        if (birthdayNeedsAgreement) {
            return (String) kakaoAccount.get("birthday_type");
        }
        return null;
    }

    /**
     * 출생연도를 "YYYY" 형태로 반환
     * @return (String) "2022"
     */
    public String getBirthyear() {
        boolean birthyearNeedsAgreement = (boolean) kakaoAccount.get("birthyear_needs_agreement");
        if (birthyearNeedsAgreement) {
            return (String) kakaoAccount.get("birthyear");
        }
        return null;
    }

    /**
     * 성별을 반환
     * female(여자) 또는 male(남자)
     * @return (String) "female"
     */
    public String getGender() {
        boolean genderNeedsAgreement = (boolean) kakaoAccount.get("gender_needs_agreement");
        if (genderNeedsAgreement) {
            return (String) kakaoAccount.get("gender");
        }
        return null;
    }

    /**
     * 전화번호 반환
     * @return (String) "+82 010-1234-5678"
     */
    public String getPhoneNumber() {
        boolean phoneNumberNeedsAgreement = (boolean) kakaoAccount.get("phone_number_needs_agreement");
        if (phoneNumberNeedsAgreement) {
            return (String) kakaoAccount.get("phone_number");
        }
        return null;
    }
}
