package brokers.mylog_backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
public class LogResponseDto {
    private long total;
    private List<Log> logs;

    @Builder
    @Getter
    @Setter
    public static class Log {
        private Long emojiCode;
        private String nickname;
        private String status;
        private LocalDateTime updatedAt;
    }
}
