package smartwin.springbasickit.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartwin.springbasickit.common.response.ApiResponse;
import smartwin.springbasickit.domain.user.dto.UserSignUpDto;
import smartwin.springbasickit.domain.user.service.SystemUserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final SystemUserService systemUserService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> signUp( @RequestBody @Valid UserSignUpDto userSignUpDto) {
        systemUserService.signUp(userSignUpDto);
        ApiResponse<Void> apiResponse = ApiResponse.success(201, "회원가입 성공", null);
        return ResponseEntity.status(apiResponse.statusCode()).body(apiResponse);
    }
}
