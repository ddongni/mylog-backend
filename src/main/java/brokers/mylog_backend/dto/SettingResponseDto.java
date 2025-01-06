package brokers.mylog_backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SettingResponseDto {

    private final String backgroundColor;

    private final String textColor;
}
