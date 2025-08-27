package smartwin.springbasickit.common.exception;

import lombok.Getter;
import smartwin.springbasickit.common.exception.code.ErrorCode;

@Getter
public class BaseException extends RuntimeException {

    private final int httpStatus;
    private final ErrorCode errorCode; // 커스텀 비즈니스 코드

    public BaseException(int httpStatus, ErrorCode errorCode, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
