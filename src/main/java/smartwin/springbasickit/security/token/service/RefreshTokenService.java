package smartwin.springbasickit.security.token.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartwin.springbasickit.common.util.DeviceUtil;
import smartwin.springbasickit.security.jwt.JwtProperties;
import smartwin.springbasickit.security.token.entity.RefreshToken;
import smartwin.springbasickit.security.token.entity.TokenType;
import smartwin.springbasickit.security.token.repository.RefreshTokenRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    /**
     * 리프레쉬 토큰 발급
     * */
    @Transactional
    public String saveRefreshToken(Long systemUserId, String userAgent) throws NoSuchAlgorithmException {

        String resolve = DeviceUtil.resolve(userAgent);

        // 고엔트로피 RT 생성 (32 bytes)
        byte[] rtBytes = new byte[32];
        new java.security.SecureRandom().nextBytes(rtBytes);
        String rt = java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(rtBytes); // 쿠키에 담을 평문 RT

        // SHA-256 해시 알고리즘으로 해시화
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] digest1 = digest.digest(rt.getBytes(StandardCharsets.UTF_8));

        StringBuilder hex = new StringBuilder();
        for (byte b : digest1) {
            hex.append(String.format("%02x", b));
        }

        // entity 생성
        RefreshToken refreshToken = RefreshToken.builder()
                                         .tokenType(TokenType.valueOf(resolve))
                                         .expiredAt(Instant.now().plus(jwtProperties.refreshDays()))
                                         .isRevoked(false)
                                         .secretHash(hex.toString())
                                         .systemUserId(systemUserId)
                                         .build();

        refreshTokenRepository.save(refreshToken);

        return rt;
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
