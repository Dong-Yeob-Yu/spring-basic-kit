package smartwin.springbasickit.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import smartwin.springbasickit.common.exception.code.ErrorCode;

/**
 * API 응답 공통 레코드 클래스
 * */
@JsonInclude(JsonInclude.Include.NON_NULL)  // null 필드 제외
public record ApiResponse<T>(
        int statusCode,
        String message,
        ErrorCode errorCode,
        T data
) {
    public static <T> ApiResponse<T> success(int code, String message, T data) {
        return new ApiResponse<>(code, message, null, data);
    }

    public static ApiResponse<Void> fail(int code, String message, ErrorCode errorCode) {
        return new ApiResponse<>(code, message, errorCode, null);
    }
}
