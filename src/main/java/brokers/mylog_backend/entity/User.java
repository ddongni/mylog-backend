package brokers.mylog_backend.entity;

import brokers.mylog_backend.dto.LogResponseDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Long emojiCode;

    private String nickname;

    private String email;

    private String status;

    private boolean leftLog;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public LogResponseDto.Log mapToLogResponseDto() {
        return LogResponseDto.Log.builder()
                .nickname(nickname)
                .emojiCode(emojiCode)
                .status(status)
                .updatedAt(updatedAt)
                .build();
    }
}
