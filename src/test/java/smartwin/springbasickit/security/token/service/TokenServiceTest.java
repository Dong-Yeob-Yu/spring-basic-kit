package smartwin.springbasickit.security.token.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseCookie;
import org.springframework.mock.web.MockHttpServletRequest;
import smartwin.springbasickit.common.exception.UnauthorizedException;
import smartwin.springbasickit.domain.auth.dto.LoginRequestDto;
import smartwin.springbasickit.domain.auth.service.UserAuthService;
import smartwin.springbasickit.domain.user.dto.UserSignUpDto;
import smartwin.springbasickit.domain.user.service.SystemUserService;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TokenServiceTest {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private EntityManager em;
    @Autowired
    private UserAuthService userAuthService;

    @Test
    void refreshToken_회전성공_이전RT재사용실패() {
        // given: 회원가입
        var signUp = new UserSignUpDto("super","qwer1234","name","nickname","aaa@aaa.com");
        systemUserService.signUp(signUp);

        // 로그인 → RT 원문 획득
        var loginReq = new MockHttpServletRequest();
        loginReq.addHeader("User-Agent", "PC");
        var loginDto = new LoginRequestDto("super", "qwer1234");
        List<ResponseCookie> cookies = userAuthService.login(loginReq, loginDto);

        String oldRt = cookies.stream()
                              .filter(c -> "RT".equals(c.getName()))
                              .findFirst().orElseThrow()
                              .getValue();

        // when: RT 회전 요청
        var refreshReq = new MockHttpServletRequest();
        refreshReq.addHeader("User-Agent", "PC");
        refreshReq.setCookies(new jakarta.servlet.http.Cookie("RT", oldRt));

        Map<String,String> res = tokenService.refreshToken(refreshReq);

        // then: 새 토큰 존재 및 RT 변경 확인
        assertNotNull(res.get("accessToken"));
        assertNotNull(res.get("refreshToken"));
        String newRt = res.get("refreshToken");
        assertNotEquals(oldRt, newRt);

        // 그리고 이전 RT 재사용 시 실패
        var reuseReq = new MockHttpServletRequest();
        reuseReq.addHeader("User-Agent", "PC");
        reuseReq.setCookies(new jakarta.servlet.http.Cookie("RT", oldRt));
        assertThrows(UnauthorizedException.class, () -> tokenService.refreshToken(reuseReq));

        // 새 RT로는 성공(선택)
        var refreshReq2 = new MockHttpServletRequest();
        refreshReq2.addHeader("User-Agent", "PC");
        refreshReq2.setCookies(new jakarta.servlet.http.Cookie("RT", newRt));
        Map<String,String> res2 = tokenService.refreshToken(refreshReq2);
        assertNotNull(res2.get("accessToken"));
        assertNotNull(res2.get("refreshToken"));
    }
}