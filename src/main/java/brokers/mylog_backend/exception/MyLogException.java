package brokers.mylog_backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MyLogException extends RuntimeException {

    private final ErrorCode errorCode;
}