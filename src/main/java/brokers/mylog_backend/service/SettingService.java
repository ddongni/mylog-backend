package brokers.mylog_backend.service;

import brokers.mylog_backend.entity.Setting;
import brokers.mylog_backend.entity.User;
import brokers.mylog_backend.exception.ErrorCode;
import brokers.mylog_backend.exception.MyLogException;
import brokers.mylog_backend.repository.SettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SettingService {

    private final SettingRepository settingRepository;

    private final UserService userService;

    @Transactional
    public void save(String email, Setting setting) {
        User user = userService.findByEmail(email);
        if(user != null) {
            Setting savedSetting = settingRepository.findByUserId(user.getId());
            if(savedSetting == null) {
                setting.setUserId(user.getId());
                settingRepository.save(setting);
            } else {
                if(setting.getBackgroundColor() != null) {
                    savedSetting.setBackgroundColor(setting.getBackgroundColor());
                }
                if(setting.getTextColor() != null) {
                    savedSetting.setTextColor(setting.getTextColor());
                }
            }
        } else {
            throw new MyLogException(ErrorCode.NOT_FOUND_DATA);
        }
    }
}
