package brokers.mylog_backend.controller;

import brokers.mylog_backend.dto.UserRequestDto;
import brokers.mylog_backend.dto.UserResponseDto;
import brokers.mylog_backend.entity.Setting;
import brokers.mylog_backend.exception.ErrorCode;
import brokers.mylog_backend.exception.MyLogException;
import brokers.mylog_backend.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/settings")
public class SettingController {

    private final SettingService settingService;

    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @PutMapping("/update")
    public void updateSettings(@RequestBody UserRequestDto userRequestDto) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
//            OAuth2User principal = (OAuth2User) authentication.getPrincipal();
//            String email = (String) principal.getAttributes().get("email");
//            settingService.save(email, setting);
//        } else {
//            throw new MyLogException(ErrorCode.FORBIDDEN);
//        }
        settingService.save(userRequestDto.getEmail(), userRequestDto.getSetting());
    }

    public String getUserEmail(OAuth2User principal) {
        if (principal == null) {
            throw new MyLogException(ErrorCode.UNAUTHORIZED);
        }
        String email = principal.getAttribute("email");
        if (email == null || email.isEmpty()) {
            throw new MyLogException(ErrorCode.NOT_FOUND_DATA);
        }
        return email;
    }
}
