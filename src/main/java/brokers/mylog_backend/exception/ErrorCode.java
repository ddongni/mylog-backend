package brokers.mylog_backend.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // 400 BAD_REQUEST
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Invalid parameter value."),
    MISSING_URL_PARAMETER(HttpStatus.BAD_REQUEST, "Required URL parameter is missing."),

    // 401 UNAUTHORIZED
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Authentication error."),

    // 403 FORBIDDEN
    FORBIDDEN(HttpStatus.FORBIDDEN, "Access is forbidden."),

    // 404 NOT_FOUND
    NOT_FOUND_DATA(HttpStatus.NOT_FOUND, "Data not found."),

    // 409 CONFLICT
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "Duplicate nickname."),

    // 500 INTERNAL_SERVER_ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error.");

    private final HttpStatus httpStatus;
    private final String errorMessage;

    ErrorCode(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}