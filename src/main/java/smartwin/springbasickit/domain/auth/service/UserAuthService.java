package smartwin.springbasickit.domain.auth.service;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import smartwin.springbasickit.common.exception.UserNotFoundException;
import smartwin.springbasickit.common.exception.UsernamePasswordNotMatchedException;
import smartwin.springbasickit.common.util.AuthUtils;
import smartwin.springbasickit.common.util.CookieUtils;
import smartwin.springbasickit.domain.auth.dto.LoginRequestDto;
import smartwin.springbasickit.domain.user.entity.SystemUserEntity;
import smartwin.springbasickit.domain.user.repository.SystemUserRepository;
import smartwin.springbasickit.domain.user.repository.SystemUserRoleRepository;
import smartwin.springbasickit.security.jwt.JwtProperties;
import smartwin.springbasickit.security.token.service.RefreshTokenService;
import smartwin.springbasickit.security.token.service.TokenService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAuthService {

    private final SystemUserRepository systemUserRepository;
    private final JwtProperties jwtProperties;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;

    private final EntityManager em;

    @Transactional
    public List<ResponseCookie> login(HttpServletRequest request, LoginRequestDto loginRequestDto) {

        SystemUserEntity systemUserEntity = systemUserRepository.findByLoginId(loginRequestDto.loginId())
                                                                .orElseThrow(UserNotFoundException::new);
        systemUserEntity.recordLastLoginAt();
        systemUserRepository.save(systemUserEntity);
        em.flush();

        if (!passwordEncoder.matches(loginRequestDto.password(), systemUserEntity.getLoginPassword())) {
            throw new UsernamePasswordNotMatchedException();
        }

        Long systemUserId = systemUserEntity.getId();
        String userAgent = request.getHeader("User-Agent");

        refreshTokenService.logoutToken(systemUserId, userAgent);

        Map<String, String> stringStringMap = tokenService.generateToken(systemUserId, userAgent);

        return CookieUtils.mapToCookieList(
                stringStringMap,
                jwtProperties.accessTtl(),
                jwtProperties.refreshDays()
        );
    }

    @Transactional
    public List<ResponseCookie> logout(HttpServletRequest request) {

        Long systemUserId = AuthUtils.getSystemUserId();
        String userAgent = request.getHeader("User-Agent");

        // RT 토큰 무효화
        refreshTokenService.logoutToken(systemUserId, userAgent);

        ResponseCookie at = CookieUtils.setResponseCookie("AT", "", Duration.ZERO);
        ResponseCookie rt = CookieUtils.setResponseCookie("RT", "", Duration.ZERO);
        return List.of(at, rt);
    }
}
