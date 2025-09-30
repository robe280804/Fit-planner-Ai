package com.fit_planner_ai.app.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fit_planner_ai.app.enums.AuthProvider;
import com.fit_planner_ai.app.enums.Role;
import com.fit_planner_ai.app.model.User;
import com.fit_planner_ai.app.repository.UserRepository;
import com.fit_planner_ai.app.security.jwt.JwtService;
import com.fit_planner_ai.app.security.model.UserDetailsImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${jwt.expiration.refresh_token}")
    private Long longExpiration;

    private final UserRepository userRepository;
    private final JwtService jwtService;

    /**
     * Success Handler post autenticazione con provider esterni
     *
     * - ottengo username (se username non è presente, gli do un valore random che l'utente potrà poi modificare)
     * - ottengo email (se email non è presente in GitHub, prendo il valore del nome dell'utente e ci aggiungo il
     *                  prefisso @github.com, l'utente potrà poi modificarlo)
     * - se l'utente non esisteva lo creo e lo salvo nel db
     * - creo il refresh e access token, il refresh lo invio in un cookie per la security, access token nell' url
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

        String name;
        String surname;
        String username;

        if (provider.equalsIgnoreCase("google")){
            name = Optional.ofNullable(oAuth2User.getAttribute("given_name"))
                    .orElse("Unkown").toString();
            surname = Optional.ofNullable(oAuth2User.getAttribute("family_name"))
                    .orElse("Unkown").toString();
            username = String.join(name + "_" + surname);
        } else if (provider.equalsIgnoreCase("github")){
            name = "GitHubUser";
            surname = UUID.randomUUID().toString();
            username = oAuth2User.getAttribute("name");
        } else {
            name = "User";
            surname = UUID.randomUUID().toString();
            username = name + "_" + surname;
        }

        if (email == null) throw new IllegalStateException("Email non fornita dal provider OAuth2: " + provider);

        UserDetailsImpl userDetails = userRepository.findByEmail(email)
                .map(UserDetailsImpl::new)
                .orElseGet(() -> {
                    User newUser = userRepository.save(User.builder()
                            .name(name)
                            .surname(surname)
                            .username(username)
                            .email(userEmail)
                            .password(null)
                            .roles(Set.of(Role.USER))
                            .provider(provider.equalsIgnoreCase("github") ? AuthProvider.GITHUB : AuthProvider.GOOGLE)
                            .build());

                    return new UserDetailsImpl(newUser);
                });

        String accessToken = jwtService.generateToken(
                true,
                userDetails.getId(),
                userDetails.getEmail(),
                userDetails.getAuthorities(),
                userDetails.getProvider());

        String refreshToken = jwtService.generateToken(
                false,
                userDetails.getId(),
                userDetails.getEmail(),
                userDetails.getAuthorities(),
                userDetails.getProvider());


        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);    // per la sicurezza XSS
        //refreshCookie.setSecure(true);   se usi HTTPS
        refreshCookie.setMaxAge(Math.toIntExact(longExpiration));
        refreshCookie.setPath("/");
        response.addCookie(refreshCookie);

        String redirectUrl = "http://localhost:5173/?token=" + accessToken;

        log.info("[OAUTH2] Accesso esegutito con successo per {} dal provider {}", email, provider);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.sendRedirect(redirectUrl);
    }
}
