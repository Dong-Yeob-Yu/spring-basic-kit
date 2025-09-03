package smartwin.springbasickit.common.exception;

import org.springframework.http.HttpStatus;
import smartwin.springbasickit.common.exception.code.ErrorCode;

public class UsernamePasswordNotMatchedException extends BaseException {

    public UsernamePasswordNotMatchedException() {
        super(HttpStatus.NOT_FOUND.value(), ErrorCode.USER_NOT_FOUND, "계정 정보를 다시 확인해 주세요.");
    }

    public UsernamePasswordNotMatchedException(String message) {
        super(HttpStatus.NOT_FOUND.value(), ErrorCode.USER_NOT_FOUND, message);
    }

    public UsernamePasswordNotMatchedException(ErrorCode errorCode, String message) {
        super(HttpStatus.NOT_FOUND.value(), errorCode, message);
    }

}
