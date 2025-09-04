package smartwin.springbasickit.security.token.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import smartwin.springbasickit.common.exception.UnauthorizedException;
import smartwin.springbasickit.common.exception.code.ErrorCode;
import smartwin.springbasickit.common.util.DeviceUtil;
import smartwin.springbasickit.security.jwt.JwtUtil;
import smartwin.springbasickit.security.token.entity.TokenType;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    /**
     * AT, RT 토큰 생성
     * @param systemUserId 사용자 ID
     * @param userAgent 유저 Agent
     * @since 2025/09/04
     * */
    public Map<String, String> generateToken(Long systemUserId, String userAgent) {

        String accessToken = jwtUtil.generateAccessToken(systemUserId, userAgent);
        String refreshToken = refreshTokenService.saveRefreshToken(systemUserId, userAgent);

        Map<String, String> map = new HashMap<>();
        map.put("accessToken", accessToken);
        map.put("refreshToken", refreshToken);

        return map;
    }

    /**
     * jwt 토큰 재발급 로직
     * 기존 RT는 만료 시키고 새로운 AT, RT 반환
     * @param request HttpServletRequest
     * @throws UnauthorizedException 토큰에 문제 있을시
     * */
    public Map<String, String> refreshToken(HttpServletRequest request) {
        String rt = jwtUtil.extractRefreshTokenFromCookies(request);

        // 토큰이 쿠키에 없으면
        if(rt == null || rt.isEmpty()){
            throw new UnauthorizedException(ErrorCode.EXPIRED_TOKEN_NULL);
        }
        String userAgent = request.getHeader("User-Agent");

        Long systemUserId = refreshTokenService.rotateToken(rt, TokenType.valueOf(DeviceUtil.resolve(userAgent)));
        return this.generateToken(systemUserId, userAgent);
    }
}
