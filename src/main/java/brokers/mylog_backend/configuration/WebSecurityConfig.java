package brokers.mylog_backend.configuration;

import brokers.mylog_backend.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
//@EnableMethodSecurity
public class WebSecurityConfig {

    private final OAuthService oAuthService;

    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
//                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .formLogin(form -> form.disable())
                .logout(logout -> logout.disable())
                .oauth2Login(oauth2 -> oauth2
                                .userInfoEndpoint(userinfo -> userinfo
                                        .userService(oAuthService))
                                .successHandler(authenticationSuccessHandler)
                )
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/v1/users/logout",
//                                "/oauth2/authorization/google",
//                                "/login/oauth2/code/google").permitAll()
//                        .anyRequest().authenticated()
//                )
                .build();
    }
}
