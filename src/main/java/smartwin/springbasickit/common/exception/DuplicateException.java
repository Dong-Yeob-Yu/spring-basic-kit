package smartwin.springbasickit.common.exception;

import org.springframework.http.HttpStatus;
import smartwin.springbasickit.common.exception.code.ErrorCode;

public class DuplicateException extends BaseException {

    public DuplicateException() {
        super(HttpStatus.CONFLICT.value(), null, "중복된 데이터 입니다.");
    }

    public DuplicateException(ErrorCode errorCode) {
        super(HttpStatus.CONFLICT.value(), errorCode, errorCode.getMessage());
    }
}
