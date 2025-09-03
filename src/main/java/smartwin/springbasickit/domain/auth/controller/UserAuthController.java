package smartwin.springbasickit.domain.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smartwin.springbasickit.common.response.ApiResponse;
import smartwin.springbasickit.domain.auth.dto.LoginRequestDto;
import smartwin.springbasickit.domain.auth.service.UserAuthService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserAuthController {

    private final UserAuthService userAuthService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Void>> login(HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid LoginRequestDto loginRequestDto) {
        List<ResponseCookie> responseCookieList = userAuthService.login(request, loginRequestDto);

        responseCookieList.forEach(responseCookie -> {
            response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        });

        ApiResponse<Void> apiResponse = ApiResponse.success(200, "로그인 성공", null);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request, HttpServletResponse response) {
        List<ResponseCookie> logout = userAuthService.logout(request);
        logout.forEach(responseCookie -> {
            response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        });

        ApiResponse<Void> apiResponse = ApiResponse.success(204, "로그아웃 성공", null);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apiResponse);
    }
}
