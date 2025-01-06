package brokers.mylog_backend.configuration;

import brokers.mylog_backend.entity.User;
import brokers.mylog_backend.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${client.url}")
    private String redirectUrl;

    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("인증성공");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String nickname = "";

        String email = null;

        Object principal = authentication.getPrincipal();
        if (principal instanceof DefaultOAuth2User) {
            DefaultOAuth2User oauthUser = (DefaultOAuth2User) principal;
            email = oauthUser.getAttribute("email");
        }
        log.info("nickname: {}, email: {}", nickname, email);
        if (email != null) {
            User user = userService.findByEmail(email);
            if(user == null) {
                response.sendRedirect(redirectUrl + "/login?isFailed=true");
                return;
            }

            nickname = user.getNickname();

            if(nickname != null) {
                String encodedNickname = URLEncoder.encode(nickname, StandardCharsets.UTF_8.toString());
                response.sendRedirect(redirectUrl + "/login/success?nickname=" + encodedNickname +"&email=" + email);
            } else {
                response.sendRedirect(redirectUrl + "/login/success?email=" + email);
            }
        } else {
            response.sendRedirect(redirectUrl + "/login?isFailed=true");
        }
    }
}