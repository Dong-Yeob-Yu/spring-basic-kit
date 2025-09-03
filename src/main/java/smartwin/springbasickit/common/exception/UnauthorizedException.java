package smartwin.springbasickit.common.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;
import smartwin.springbasickit.common.exception.code.ErrorCode;

@Getter
public class UnauthorizedException extends AuthenticationException {

    private ErrorCode errorCode;

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
