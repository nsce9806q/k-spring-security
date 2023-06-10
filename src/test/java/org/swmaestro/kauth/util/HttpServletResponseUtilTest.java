package org.swmaestro.kauth.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.swmaestro.kauth.dto.UsernamePasswordLoginRequest;

import java.io.IOException;

class HttpServletResponseUtilTest {

    @DisplayName("헤더 설정 테스트")
    @Test
    void setHeader() {
        HttpServletResponse response = new MockHttpServletResponse();

        HttpServletResponseUtil.setHeader(response, "name", "value");

        Assertions.assertEquals("value", response.getHeader("name"));
    }

    @DisplayName("JSON body 설정 테스트")
    @Test
    void setJsonBody() throws IOException {
        UsernamePasswordLoginRequest loginRequest = new UsernamePasswordLoginRequest();
        loginRequest.setUsername("username");
        loginRequest.setPassword("password");

        HttpServletResponse response = new MockHttpServletResponse();
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        HttpServletResponseUtil.setJsonBody(responseWrapper, loginRequest);

        byte[] contentAsByteArray = responseWrapper.getContentAsByteArray();
        String responseStr = new String(contentAsByteArray, responseWrapper.getCharacterEncoding());
        UsernamePasswordLoginRequest responseLoginRequest = new ObjectMapper().readValue(responseStr, UsernamePasswordLoginRequest.class);

        Assertions.assertEquals("application/json;charset=UTF-8", response.getContentType());
        Assertions.assertEquals("UTF-8", response.getCharacterEncoding());
        Assertions.assertEquals("username", responseLoginRequest.getUsername());
        Assertions.assertEquals("password", responseLoginRequest.getPassword());
    }

    @DisplayName("401 응답 설정 테스트")
    @Test
    void setUnauthorizedResponse() throws IOException {
        RuntimeException exception = new RuntimeException("unauthorized");

        HttpServletResponse response = new MockHttpServletResponse();
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        HttpServletResponseUtil.setUnauthorizedResponse(responseWrapper, exception);

        byte[] contentAsByteArray = responseWrapper.getContentAsByteArray();
        String responseStr = new String(contentAsByteArray, responseWrapper.getCharacterEncoding());
        RuntimeException responseException = new ObjectMapper().readValue(responseStr, RuntimeException.class);

        Assertions.assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        Assertions.assertEquals("unauthorized", responseException.getMessage());
    }

    @DisplayName("Response Cookie 설정 테스트")
    @Test
    void setCookie() {
        HttpServletResponse response = new MockHttpServletResponse();

        HttpServletResponseUtil.setCookie(
                response,
                "refresh_token",
                "token",
                "/",
                3600,
                "localhost",
                Cookie.SameSite.NONE
        );

        String responseCookie = response.getHeader(HttpHeaders.SET_COOKIE);

        Assertions.assertNotNull(responseCookie);
        Assertions.assertTrue(responseCookie.contains("refresh_token=token"));
        Assertions.assertTrue(responseCookie.contains("Path=/"));
        Assertions.assertTrue(responseCookie.contains("Expires="));
        Assertions.assertTrue(responseCookie.contains("Domain=localhost"));
        Assertions.assertTrue(responseCookie.contains("SameSite=NONE"));
    }
}
