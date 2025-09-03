package smartwin.springbasickit.common.exception;

import org.springframework.http.HttpStatus;
import smartwin.springbasickit.common.exception.code.ErrorCode;

public class UserRoleNotFoundException extends BaseException {

    public UserRoleNotFoundException() {
        super(HttpStatus.NOT_FOUND.value(), ErrorCode.USER_NOT_FOUND, "유저 권한을 찾을 수 없습니다.");
    }

    public UserRoleNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND.value(), ErrorCode.USER_NOT_FOUND, message);
    }

    public UserRoleNotFoundException(ErrorCode errorCode, String message) {
        super(HttpStatus.NOT_FOUND.value(), errorCode, message);
    }

}
