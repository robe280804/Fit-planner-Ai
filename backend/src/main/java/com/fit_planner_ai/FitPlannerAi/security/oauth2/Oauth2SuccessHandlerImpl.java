package com.fit_planner_ai.FitPlannerAi.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fit_planner_ai.FitPlannerAi.model.AuthProvider;
import com.fit_planner_ai.FitPlannerAi.model.Roles;
import com.fit_planner_ai.FitPlannerAi.model.User;
import com.fit_planner_ai.FitPlannerAi.repository.UserRepository;
import com.fit_planner_ai.FitPlannerAi.security.model.UserDetailsImpl;
import com.fit_planner_ai.FitPlannerAi.security.jwt.JwtService;
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
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class Oauth2SuccessHandlerImpl implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    /**
     * Success Handler post autenticazione con provider esterni
     *
     * - ottengo username (se username non è presente, gli do un valore random che l'utente potrà poi modificare)
     * - ottengo email (se email non è presente in GitHub, prendo il valore del nome dell'utente e ci aggiungo il
     *                  prefisso @github.com, l'utente potrà poi modificarlo)
     * - se l'utente non esisteva lo creo e lo salvo nel db, e creo il token che invierò nel body della risposta
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String provider = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");

        String userEmail = oAuth2User.getAttribute("email") != null ?
                email : oAuth2User.getAttribute("login") + "@github.com";

        String userName = provider.equalsIgnoreCase("github")
                ? oAuth2User.getAttribute("login")
                : Optional.ofNullable(oAuth2User.getAttribute("name"))
                .orElse("user" + UUID.randomUUID()).toString();

        if (email == null) throw new IllegalStateException("Email non fornita dal provider OAuth2: " + provider);

        UserDetailsImpl userDetails = userRepository.findByEmail(email)
                .map(UserDetailsImpl::new)
                .orElseGet(() -> {
                    User newUser = userRepository.save(User.builder()
                            .userName(userName)
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
