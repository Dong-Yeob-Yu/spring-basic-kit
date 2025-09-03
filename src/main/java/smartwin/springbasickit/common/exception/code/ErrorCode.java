package smartwin.springbasickit.common.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    DUPLICATE_ID("409_ID", "이미 사용 중인 ID입니다.", HttpStatus.CONFLICT),
    USER_NOT_FOUND("404_USER", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    USERID_NOT_MATCH("404_USERID", "아이디 또는 비밀번호가 올바르지 않습니다.", HttpStatus.NOT_FOUND),
    INVALID_TOKEN("401_TOKEN", "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN_NULL("401_TOKEN_NULL", "토큰이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_AT_TOKEN("401_AT_EXPIRED", "AT가 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_RT_TOKEN("401_RT_EXPIRED", "RT가 만료되었습니다.", HttpStatus.UNAUTHORIZED);

    private final String code;
    private final String message;
    private final HttpStatus status;

}
