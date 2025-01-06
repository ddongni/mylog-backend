package brokers.mylog_backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserResponseDto {

    private Long emojiCode;

    private String status;

    private SettingResponseDto settings;
}
