package smartwin.springbasickit.common.exception;

import org.springframework.http.HttpStatus;
import smartwin.springbasickit.common.exception.code.ErrorCode;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND.value(), ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.");
    }

    public UserNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND.value(), ErrorCode.USER_NOT_FOUND, message);
    }

    public UserNotFoundException(ErrorCode errorCode, String message) {
        super(HttpStatus.NOT_FOUND.value(), errorCode, message);
    }

}
