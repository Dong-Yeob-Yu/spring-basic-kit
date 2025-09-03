package smartwin.springbasickit.security.token.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import smartwin.springbasickit.security.jwt.JwtUtil;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public Map<String, String> generateToken(Long systemUserId, String userAgent) throws NoSuchAlgorithmException {

        String accessToken = jwtUtil.generateAccessToken(systemUserId, userAgent);
        String refreshToken = refreshTokenService.saveRefreshToken(systemUserId, userAgent);

        Map<String, String> map = new HashMap<>();
        map.put("accessToken", accessToken);
        map.put("refreshToken", refreshToken);

        return map;
    }
}
