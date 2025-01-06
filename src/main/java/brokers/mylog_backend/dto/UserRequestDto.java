package brokers.mylog_backend.dto;

import brokers.mylog_backend.entity.Setting;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserRequestDto {

    private String email;

    private Setting setting;

    private String nickname;
}
