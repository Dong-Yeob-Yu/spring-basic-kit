package smartwin.springbasickit.security.token.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartwin.springbasickit.common.exception.UnauthorizedException;
import smartwin.springbasickit.common.exception.code.ErrorCode;
import smartwin.springbasickit.common.util.DeviceUtil;
import smartwin.springbasickit.common.util.TokenUtils;
import smartwin.springbasickit.security.jwt.JwtProperties;
import smartwin.springbasickit.security.jwt.JwtUtil;
import smartwin.springbasickit.security.token.entity.RefreshToken;
import smartwin.springbasickit.security.token.entity.TokenType;
import smartwin.springbasickit.security.token.repository.RefreshTokenRepository;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;
    private final JwtUtil jwtUtil;

    /**
     * 리프레쉬 토큰 발급
     * */
    @Transactional
    public String saveRefreshToken(Long systemUserId, String userAgent) {

        String resolve = DeviceUtil.resolve(userAgent);

        // 고엔트로피 RT 생성 (32 bytes)
        String rt = TokenUtils.generateRefreshToken();
        // SHA-256 해시 알고리즘으로 해시화
        String hex = TokenUtils.hashToken(rt);

        // entity 생성
        RefreshToken refreshToken = RefreshToken.builder()
                                         .tokenType(TokenType.valueOf(resolve))
                                         .expiredAt(Instant.now().plus(jwtProperties.refreshDays()))
                                         .isRevoked(false)
                                         .secretHash(hex)
                                         .systemUserId(systemUserId)
                                         .build();

        refreshTokenRepository.save(refreshToken);

        return rt;
    }

    @Transactional
    public Long rotateToken(String rawRt, TokenType tokenType) {

        String secretToken = TokenUtils.hashToken(rawRt);

        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findBySecretHashAndExpiredAtAfterAndIsRevokedFalseAndTokenType(
                secretToken,
                Instant.now(),
                tokenType
        );

        if(optionalRefreshToken.isPresent()){
            RefreshToken refreshToken = optionalRefreshToken.get();
            this.updateRevoked(refreshToken.getId());
            return refreshToken.getSystemUserId();
        } else {
            throw new UnauthorizedException(ErrorCode.EXPIRED_RT_TOKEN);
        }
    }

    /**
     * 토큰 만료
     * */
    @Transactional
    public void updateRevoked(Long id) {
        refreshTokenRepository.updateRevokedTrueById(id);
    }

    @Transactional
    public void logoutToken(Long systemUserId, String userAgent) {
        String resolve = DeviceUtil.resolve(userAgent);
        refreshTokenRepository.updateTokenAtLogout(systemUserId, TokenType.valueOf(resolve), Instant.now());
    }

}
