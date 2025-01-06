package brokers.mylog_backend.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(MyLogException.class)
    public ResponseEntity<ErrorResponse> handleMyLogException(MyLogException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                e.getErrorCode().getHttpStatus().value(),
                e.getErrorCode().name(),
                e.getErrorCode().getErrorMessage());
        return new ResponseEntity<>(errorResponse, e.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.MISSING_URL_PARAMETER.getHttpStatus().value(),
                ErrorCode.MISSING_URL_PARAMETER.name(),
                ErrorCode.MISSING_URL_PARAMETER.getErrorMessage());
        return new ResponseEntity<>(errorResponse, ErrorCode.MISSING_URL_PARAMETER.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.INVALID_PARAMETER.getHttpStatus().value(),
                ErrorCode.INVALID_PARAMETER.name(),
                ErrorCode.INVALID_PARAMETER.getErrorMessage());
        return new ResponseEntity<>(errorResponse, ErrorCode.INVALID_PARAMETER.getHttpStatus());
    }
}