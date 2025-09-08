package smartwin.springbasickit.security.token.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import smartwin.springbasickit.common.exception.DuplicateException;
import smartwin.springbasickit.common.exception.UnauthorizedException;
import smartwin.springbasickit.common.exception.code.ErrorCode;
import smartwin.springbasickit.common.util.DeviceUtil;
import smartwin.springbasickit.domain.user.repository.SystemUserRepository;
import smartwin.springbasickit.security.jwt.JwtUtil;
import smartwin.springbasickit.security.token.entity.TokenType;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final SystemUserRepository systemUserRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * AT, RT 토큰 생성
     * @param systemUserId 사용자 ID
     * @param userAgent 유저 Agent
     * @since 2025/09/04
     * */
    public Map<String, String> generateToken(Long systemUserId, String userAgent) {

        List<String> roles = systemUserRepository.findRolesByUserId(systemUserId);

        String accessToken = jwtUtil.generateAccessToken(systemUserId, userAgent, roles);
        String refreshToken = refreshTokenService.saveRefreshToken(systemUserId, userAgent);

        Map<String, String> map = new HashMap<>();
        map.put("accessToken", accessToken);
        map.put("refreshToken", refreshToken);

        return map;
    }

    /**
     * jwt 토큰 재발급 로직
     * 기존 RT는 만료 시키고 새로운 AT, RT 반환
     * 레디스 사용으로 동시성 문제 원자 처리
     * @param request HttpServletRequest
     * @throws UnauthorizedException 토큰에 문제 있을시
     * */
    public Map<String, String> refreshToken(HttpServletRequest request) {
        String rt = jwtUtil.extractRefreshTokenFromCookies(request);

        // 토큰이 쿠키에 없으면
        if(rt == null || rt.isEmpty()){
            throw new UnauthorizedException(ErrorCode.EXPIRED_TOKEN_NULL);
        }

        // rt에 해당하는 키가 레디스에 존재하면 FALSE -> 아무처리안함, 존재하지않으면 TRUE -> 값을 1로 설정, TTL은 5초
        String lockKey = "RT:" + rt;
        String owner = UUID.randomUUID().toString();
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, owner, 5, TimeUnit.SECONDS);

        // 락 획득에 실패하면 409 에러
        if(Boolean.FALSE.equals(lock)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_RT);
        }

        try {
            String userAgent = request.getHeader("User-Agent");

            Long systemUserId = refreshTokenService.rotateToken(rt, TokenType.valueOf(DeviceUtil.resolve(userAgent)));
            return this.generateToken(systemUserId, userAgent);
        } finally {
            // 코드 발급 후 루아 스크립트로 redis 락 해제
            DefaultRedisScript<Long> script = new DefaultRedisScript<>(
                    "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end",
                    Long.class
            );
            stringRedisTemplate.execute(script, List.of(lockKey), owner);
        }
    }
}
