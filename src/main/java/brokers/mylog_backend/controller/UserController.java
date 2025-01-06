package brokers.mylog_backend.controller;

import brokers.mylog_backend.dto.UserRequestDto;
import brokers.mylog_backend.dto.UserResponseDto;
import brokers.mylog_backend.entity.User;
import brokers.mylog_backend.exception.ErrorCode;
import brokers.mylog_backend.exception.MyLogException;
import brokers.mylog_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @PostMapping("/init")
    public ResponseEntity<UserResponseDto> init(@RequestBody UserRequestDto userRequestDto) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        log.info("authentication : {}", authentication.toString());
//        log.info("authentication.getPrincipal : {}", authentication.getPrincipal());
//        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
//            OAuth2User principal = (OAuth2User) authentication.getPrincipal();
//            String email = (String) principal.getAttributes().get("email");
//            return userService.findUserInfo(email);
//        } else {
//            throw new MyLogException(ErrorCode.FORBIDDEN);
//        }
        UserResponseDto response =  userService.findUserInfo(userRequestDto.getEmail());
//        return ResponseEntity.ok(response);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @PutMapping("/nickname")
    public void updateNickname(@RequestBody UserRequestDto userRequestDto) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
//            OAuth2User principal = (OAuth2User) authentication.getPrincipal();
//            String email = (String) principal.getAttributes().get("email");
//            userService.updateNickname(email, user.getNickname());
//        } else {
//            throw new MyLogException(ErrorCode.FORBIDDEN);
//        }
        userService.updateNickname(userRequestDto.getEmail(), userRequestDto.getNickname());
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

    @PostMapping("/logout")
    public void logout() {
//        String email = getUserEmail(principal);
        SecurityContextHolder.clearContext();
    }
}