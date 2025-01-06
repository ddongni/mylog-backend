package brokers.mylog_backend.service;

import brokers.mylog_backend.dto.SettingResponseDto;
import brokers.mylog_backend.dto.UserResponseDto;
import brokers.mylog_backend.entity.Setting;
import brokers.mylog_backend.entity.User;
import brokers.mylog_backend.exception.ErrorCode;
import brokers.mylog_backend.exception.MyLogException;
import brokers.mylog_backend.repository.SettingRepository;
import brokers.mylog_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final SettingRepository settingRepository;

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElse(null);
    }

    @Transactional
    public void updateNickname(String email, String nickname) {
        User duplicateNicknameUser = findByNickname(nickname);
        if(duplicateNicknameUser != null) {
            throw new MyLogException(ErrorCode.DUPLICATE_NICKNAME);
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new MyLogException(ErrorCode.NOT_FOUND_DATA));
        user.setNickname(nickname);
    }

    public User findByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public UserResponseDto findUserInfo(String email) {
        User user = findByEmail(email);
        if(user == null) return null;
        log.info("user id : {}, email : {}", user.getId(), email);
        Setting setting = settingRepository.findByUserId(user.getId());
        SettingResponseDto settingResponseDto = null;
        if(setting != null) {
            settingResponseDto = SettingResponseDto.builder()
                    .backgroundColor(setting.getBackgroundColor())
                    .textColor(setting.getTextColor())
                    .build();
        }
        return UserResponseDto.builder()
                .emojiCode(user.getEmojiCode())
                .status(user.getStatus())
                .settings(settingResponseDto)
                .build();
    }
}