package brokers.mylog_backend.service;

import brokers.mylog_backend.dto.LogResponseDto;
import brokers.mylog_backend.entity.User;
import brokers.mylog_backend.exception.ErrorCode;
import brokers.mylog_backend.exception.MyLogException;
import brokers.mylog_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LogService {

    private final UserRepository userRepository;

    @Async
    @Transactional
    public void save(User user, User message, StompHeaderAccessor accessor) {
        user.setEmojiCode(message.getEmojiCode());
        user.setStatus(message.getStatus());
        user.setUpdatedAt(message.getUpdatedAt());
        user.setLeftLog(true);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public LogResponseDto findAll(Integer page, Integer size) {
        Pageable pageable = Pageable.unpaged();
        if (page != null && size != null) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "updatedAt"));
        } else {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "updatedAt"));
        }
        Page<User> allLogs = userRepository.findAllByLeftLog(true, pageable);

        List<LogResponseDto.Log> logs = allLogs.stream().map(user -> user.mapToLogResponseDto())
                .collect(Collectors.toList());
        return LogResponseDto.builder()
                .total(allLogs.getTotalElements())
                .logs(logs)
                .build();
    }
}
