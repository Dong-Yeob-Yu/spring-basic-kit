package smartwin.springbasickit.common.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    DUPLICATE_EMAIL("409EMAIL", "이미 사용 중인 이메일입니다.", HttpStatus.CONFLICT),
    USER_NOT_FOUND("404USER", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_TOKEN("401TOKEN", "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED);

    private final String code;
    private final String message;
    private final HttpStatus status;

}
