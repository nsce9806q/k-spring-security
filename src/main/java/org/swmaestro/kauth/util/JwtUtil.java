package org.swmaestro.kauth.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * JWT 발급/검증
 * @author ChangEn Yea
 */
@Component
public class JwtUtil {

    @Value("${kauth.jwt.secret:kauth}")
    private String JWT_SECRET;

    @Value("${kauth.jwt.prefix:bearer}")
    private String JWT_PREFIX;

    @Value("${kauth.jwt.access-token-expiration-time:1800000}")
    private Long ACCESS_TOKEN_EXPIRATION_TIME;

    @Value("${kauth.jwt.refresh-token-expiration-time:2592000000}")
    private Long REFRESH_TOKEN_EXPIRATION_TIME;

    private String createToken(String sub, Long expirationTime) {
        return JWT_PREFIX + " " + JWT.create()
            .withSubject(sub)
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
            .sign(Algorithm.HMAC512(JWT_SECRET));
    }

    public String createAccessToken(String username) {
        return createToken(username, ACCESS_TOKEN_EXPIRATION_TIME);
    }

    public String createRefreshToken(String username) {
        return createToken(username, REFRESH_TOKEN_EXPIRATION_TIME);
    }

    public String verifyToken(String token) {
        return JWT.require(Algorithm.HMAC512(JWT_SECRET)).build()
            .verify(token.replace(JWT_PREFIX, ""))
            .getSubject();
    }
}
