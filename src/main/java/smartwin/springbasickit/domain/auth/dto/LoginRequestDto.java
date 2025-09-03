package smartwin.springbasickit.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
       @NotBlank String loginId,
       @NotBlank String password
) {
}
