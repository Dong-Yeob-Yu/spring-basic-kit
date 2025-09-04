package smartwin.springbasickit.security.login;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseCookie;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;
import smartwin.springbasickit.common.util.TokenUtils;
import smartwin.springbasickit.domain.auth.dto.LoginRequestDto;
import smartwin.springbasickit.domain.auth.service.UserAuthService;
import smartwin.springbasickit.domain.user.dto.UserSignUpDto;
import smartwin.springbasickit.domain.user.service.SystemUserService;
import smartwin.springbasickit.security.jwt.JwtUtil;
import smartwin.springbasickit.security.token.entity.RefreshToken;
import smartwin.springbasickit.security.token.repository.RefreshTokenRepository;

import java.util.List;

@SpringBootTest
public class UserLoginTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private EntityManager em;
    @Autowired
    private UserAuthService userAuthService;


    @DisplayName("토큰 비교")
    @Test
    @Transactional
    public void 로그인RT_디비SECRET_KEY비교() throws Exception {

        // 회원가입
        UserSignUpDto userSignUpDto = new UserSignUpDto("super", "qwer1234", "name", "nickanme", "aaa@aaa.com");
        systemUserService.signUp(userSignUpDto);

        em.flush();
        em.clear();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("User-Agent", "PC");

        LoginRequestDto loginRequestDto = new LoginRequestDto("super", "qwer1234");

        List<ResponseCookie> login = userAuthService.login(request, loginRequestDto);

        ResponseCookie responseCookie = login.get(1);
        String value = responseCookie.getValue();

        em.flush();
        em.clear();

        RefreshToken refreshToken = refreshTokenRepository.findById(1L).orElseThrow();

        boolean b = TokenUtils.equalsToken(value, refreshToken.getSecretHash());

        Assertions.assertTrue(b);

        System.out.println(" 성공 ");

    }
}
