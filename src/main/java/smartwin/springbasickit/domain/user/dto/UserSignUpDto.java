package smartwin.springbasickit.domain.user.dto;

import jakarta.validation.constraints.Email;

public record UserSignUpDto(
        String loginId,
        String password,
        String name,
        String nickname,
        @Email String email
) {
}
