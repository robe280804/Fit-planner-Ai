package com.fit_planner_ai.FitPlannerAi.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fit_planner_ai.FitPlannerAi.model.AuthProvider;
import com.fit_planner_ai.FitPlannerAi.model.Roles;
import com.fit_planner_ai.FitPlannerAi.model.User;
import com.fit_planner_ai.FitPlannerAi.repository.UserRepository;
import com.fit_planner_ai.FitPlannerAi.security.model.UserDetailsImpl;
import com.fit_planner_ai.FitPlannerAi.security.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class Oauth2SuccessHandlerImpl implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String provider = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        if (email == null) throw new IllegalStateException("Email non fornita dal provider OAuth2: " + provider);

        UserDetailsImpl userDetails = userRepository.findByEmail(email)
                .map(UserDetailsImpl::new)
                .orElseGet(() -> {
                    User newUser = userRepository.save(User.builder()
                            .email(email)
                            .password(null)
                            .roles(Set.of(Roles.USER))
                            .provider(provider.equalsIgnoreCase("github") ? AuthProvider.GITHUB : AuthProvider.GOOGLE)
                            .build());

                    return new UserDetailsImpl(newUser);
                });

        String token = jwtService.generateToken(
                userDetails.getId(), userDetails.getEmail(), userDetails.getAuthorities(), userDetails.getProvider()
        );

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, String> responseBody = Map.of(
                "email", userDetails.getEmail(),
                "token", token,
                "provider", provider
        );

        log.info("[OAUTH2] Accesso esegutito con successo per {} dal provider {}", email, provider);
        new ObjectMapper().writeValue(response.getWriter(), responseBody);
    }
}
