package smartwin.springbasickit.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import smartwin.springbasickit.common.response.ApiResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.warn("Exception: {}", e.getMessage());
        return ResponseEntity
                .internalServerError()
                .body(ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("IllegalArgumentException: {}", e.getMessage(), e);
        return ResponseEntity
                .internalServerError()
                .body(ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null));
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleBaseException(BaseException e) {
        log.warn("BaseException: {}", e.getMessage());
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ApiResponse.fail(e.getHttpStatus(), e.getMessage(), e.getErrorCode()));
    }

}
