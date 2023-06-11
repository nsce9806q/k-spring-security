package org.swmaestro.kauth.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HttpServletResponseUtil {

    private final ObjectMapper objectMapper;

    public HttpServletResponseUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // setHeader sets header in HttpServletResponse
    public void setHeader(HttpServletResponse response, String name, String value) {
        response.setHeader(name, value);
    }

    // setJsonBody sets json body in HttpServletResponse
    public void setJsonBody(HttpServletResponse response, Object body) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String responseBody = objectMapper.writeValueAsString(body);

        response.getWriter().write(responseBody);
    }

    // setUnauthorizedResponse sets 401 Unauthorized response in HttpServletResponse
    public void setUnauthorizedResponse(HttpServletResponse response, Exception exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String responseBody = objectMapper.writeValueAsString(exception);

        response.getWriter().write(responseBody);
    }

    // setCookie sets cookie in HttpServletResponse
    public void setCookie(
            HttpServletResponse response,
            String name,
            String value,
            String path,
            int maxAge,
            String domain,
            Cookie.SameSite sameSite
    ) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path(path)
                .httpOnly(true)
                .secure(true)
                .maxAge(maxAge)
                .sameSite(sameSite.toString())
                .domain(domain)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
