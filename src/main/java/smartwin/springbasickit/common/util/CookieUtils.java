package smartwin.springbasickit.common.util;

import org.springframework.http.ResponseCookie;

public class CookieUtils {

    /**
     * 쿠키 공통 생성
     * */
    public static ResponseCookie setResponseCookie(String name, String value, Long maxAgeSeconds) {
        return ResponseCookie.from(name, value)
                             .httpOnly(true)
                             .secure(false)   // https 에서만 쿠키 전송, 개발시 false
                             .path("/")
                             .maxAge(maxAgeSeconds)
                             .sameSite("None") // 크로스 사이트 요청에 쿠키포함 여부
                             .build();
    }

}
