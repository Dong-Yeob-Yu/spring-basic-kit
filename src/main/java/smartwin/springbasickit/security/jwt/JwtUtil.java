package smartwin.springbasickit.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import smartwin.springbasickit.common.util.DeviceUtil;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtil {

    private final JwtProperties jwtProperties;

    /**
     * Access Token 발급
     *
     * @param systemUserId - 유저 PK
     * @return String
     *
     */
    public String generateAccessToken(Long systemUserId, String userAgent, List<String> roles) {

        String device = DeviceUtil.resolve(userAgent);
        Instant now = Instant.now();

        String[] roleArray = roles == null ? new String[0] : roles.toArray(new String[0]);

        return JWT.create()
                  .withJWTId(UUID.randomUUID().toString())
                  .withSubject(String.valueOf(systemUserId))
                  .withClaim("device", device)
                  .withClaim("type", "access")
                  .withArrayClaim("roles", roleArray)
                  .withIssuer(jwtProperties.issuer())
                  .withIssuedAt(now)
                  .withExpiresAt(Date.from(now.plus(jwtProperties.accessTtl())))
                  .sign(Algorithm.HMAC256(jwtProperties.secret().getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 토큰에서 Claim 추출
     *
     * @param token     - accessToken
     * @param claim     - 추출한 claim
     * @param claimType - 추출할 타입
     * @return claimType
     *
     */
    public <T> T extractClaim(String token, String claim, Class<T> claimType) {

        // 토큰, claim 값이 없으면 null 반환
        if (token == null || token.isEmpty() || claim == null || claim.isEmpty()) {
            return null;
        }

        try {
            JWT.require(Algorithm.HMAC256(jwtProperties.secret().getBytes(StandardCharsets.UTF_8)))
               .build()
               .verify(token);
        } catch (JWTVerificationException exception) {
            return null;
        }

        Claim jwtClaim = JWT.decode(token).getClaim(claim);

        if (claimType == String.class) {
            return claimType.cast(jwtClaim.asString());
        } else if (claimType == Integer.class) {
            return claimType.cast(jwtClaim.asInt());
        } else if (claimType == Long.class) {
            return claimType.cast(jwtClaim.asLong());
        } else if (claimType == List.class) {
            // roles 같은 경우
            return claimType.cast(jwtClaim.asList(String.class));
        }

        throw new IllegalArgumentException("Unsupported claim type: " + claimType.getSimpleName());
    }

    /**
     * HttpServletRequest 에서 Access Token 추출
     *
     * @param httpServletRequest - HttpServletRequest
     * @return String
     *
     */
    public String extractAccessTokenFromCookies(HttpServletRequest httpServletRequest) {

        // at 가 없으면 null 반환
        if (httpServletRequest.getCookies() == null) {
            return null;
        }

        // rt_secret
        return Arrays.stream(httpServletRequest.getCookies())
                     .filter(cookie -> "AT".equals(cookie.getName()))
                     .map(Cookie::getValue)
                     .findFirst()
                     .orElse(null);
    }

    /**
     * HttpServletRequest 에서 Refresh Token 추출
     *
     * @param httpServletRequest - HttpServletRequest
     * @return String
     *
     */
    public String extractRefreshTokenFromCookies(HttpServletRequest httpServletRequest) {

        // at 가 없으면 null 반환
        if (httpServletRequest.getCookies() == null) {
            return null;
        }

        // rt_secret
        return Arrays.stream(httpServletRequest.getCookies())
                     .filter(cookie -> "RT".equals(cookie.getName()))
                     .map(Cookie::getValue)
                     .findFirst()
                     .orElse(null);
    }

    /**
     * 토큰 만료 확인, 검증
     *
     * @param token - AccessToken
     * @return boolean
     *
     */
    public boolean validateAccessToken(String token) {

        if (token == null || token.isEmpty()) {
            return false;
        }

        try {
            JWT.require(Algorithm.HMAC256(jwtProperties.secret().getBytes(StandardCharsets.UTF_8)))
               .build()
               .verify(token);
            return true;
        } catch (JWTVerificationException verificationException) {
            log.warn("[JWT 위조감지] {}", verificationException.getMessage());
        }

        return false;
    }

    public String extractSystemUserId(String token) {

        if (token == null || token.isEmpty()) {
            return null;
        }

        return JWT.decode(token).getSubject();
    }


}
