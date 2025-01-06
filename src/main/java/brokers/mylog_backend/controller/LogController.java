package brokers.mylog_backend.controller;

import brokers.mylog_backend.dto.LogResponseDto;
import brokers.mylog_backend.entity.User;
import brokers.mylog_backend.exception.ErrorCode;
import brokers.mylog_backend.exception.MyLogException;
import brokers.mylog_backend.service.LogService;
import brokers.mylog_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/logs")
public class LogController {

    private final SimpMessageSendingOperations sendingOperations;

    private final LogService logService;

    private final UserService userService;

    @MessageMapping("/message")
    public void message(User message, StompHeaderAccessor accessor) {
        Principal principal = accessor.getUser();
        if (principal == null) {
            throw new MyLogException(ErrorCode.UNAUTHORIZED);
        }

        User user = null;
        if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) principal;
            Object principalObject = authToken.getPrincipal();

            if (principalObject instanceof OAuth2User) {
                OAuth2User oauthUser = (OAuth2User) principalObject;
                String email = oauthUser.getAttribute("email");

                user = userService.findByEmail(email);
                if (user == null) {
                    throw new MyLogException(ErrorCode.FORBIDDEN);
                }
            } else {
                throw new MyLogException(ErrorCode.UNAUTHORIZED);
            }
        }
        if (user == null) {
            throw new MyLogException(ErrorCode.UNAUTHORIZED);
        }

        sendingOperations.convertAndSend("/topic/2025", message);
        logService.save(user, message, accessor);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @GetMapping("/all")
    public LogResponseDto getAllLogs(@RequestParam(required = false) Integer page,
                                     @RequestParam(required = false) Integer size) {
        return logService.findAll(page, size);
    }
}
