package org.swmaestro.kauth.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import java.io.IOException;

public class HttpServletResponseUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // setHeader sets header in HttpServletResponse
    public static void setHeader(HttpServletResponse response, String name, String value) {
        response.setHeader(name, value);
    }

    // setJsonBody sets json body in HttpServletResponse
    public static void setJsonBody(HttpServletResponse response, Object body) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String responseBody = objectMapper.writeValueAsString(body);

        response.getWriter().write(responseBody);
    }

    // setUnauthorizedResponse sets 401 Unauthorized response in HttpServletResponse
    public static void setUnauthorizedResponse(HttpServletResponse response, Exception exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String responseBody = objectMapper.writeValueAsString(exception);

        response.getWriter().write(responseBody);
    }

    // setCookie sets cookie in HttpServletResponse
    public static void setCookie(
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
