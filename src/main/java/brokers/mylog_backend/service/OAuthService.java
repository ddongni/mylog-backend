package brokers.mylog_backend.service;

import brokers.mylog_backend.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OAuthService extends DefaultOAuth2UserService {

    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        String email = String.valueOf(oAuth2User.getAttributes().get("email"));
        User user = userService.findByEmail(email);
        if (user == null) {
            saveUser(oAuth2User);
        }
        return new DefaultOAuth2User(
                AuthorityUtils.createAuthorityList("ROLE_USER"),
                oAuth2User.getAttributes(),
                userNameAttributeName
        );
    }

    public void saveUser(OAuth2User oAuth2User) {
        Object email = oAuth2User.getAttributes().get("email");
        if (email == null) {
            throw new IllegalArgumentException("No 'email' found in OAuth2User attributes");
        }
        String emailString = String.valueOf(email);
        userService.save(User.builder().email(emailString).build());
    }
}