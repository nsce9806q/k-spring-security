package org.swmaestro.kauth.dto;

/**
 * username + password 로그인 요청 body
 * @author ChangEn Yea
 */
public class UsernamePasswordLoginRequest {

    private String username;

    private String password;

    public UsernamePasswordLoginRequest() {}

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
