package smartwin.springbasickit.common.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {
    public BadRequestException() {
        super(HttpStatus.BAD_REQUEST.value(), null, "잘못된 요청입니다.");
    }

    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), null, message);
    }
}
