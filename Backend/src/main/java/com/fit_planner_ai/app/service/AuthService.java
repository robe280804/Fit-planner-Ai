package com.fit_planner_ai.app.service;

import com.fit_planner_ai.app.dto.LoginRequestDto;
import com.fit_planner_ai.app.dto.LoginResponseDto;
import com.fit_planner_ai.app.dto.RegisterRequestDto;
import com.fit_planner_ai.app.dto.RegisterResponseDto;
import com.fit_planner_ai.app.enums.AuthProvider;
import com.fit_planner_ai.app.exception.UserAlredyRegister;
import com.fit_planner_ai.app.exception.WrongAuthProvider;
import com.fit_planner_ai.app.mapper.UserMapper;
import com.fit_planner_ai.app.model.RefreshToken;
import com.fit_planner_ai.app.model.User;
import com.fit_planner_ai.app.repository.RefreshTokenRepository;
import com.fit_planner_ai.app.repository.UserRepository;
import com.fit_planner_ai.app.security.jwt.JwtService;
import com.fit_planner_ai.app.security.model.UserDetailsImpl;
import com.fit_planner_ai.app.security.service.UserDetailsServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    @Value("${jwt.expiration.refresh_token}")
    private Long longExpiration;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserDetailsServiceImpl userDetailsService;

    public RegisterResponseDto register(RegisterRequestDto request) {
        log.info("[REGISTER] Registrazione in esecuzione per {} ", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())){
            throw new UserAlredyRegister("Email già registrata");
        }

        User newUser = User.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .username(request.getName() + "_" + request.getSurname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(request.getRoles())
                .provider(AuthProvider.LOCALE)
                .build();

        User savedUser = userRepository.save(newUser);

        log.info("[REGISTER] Registrazione avvenuta con successo per {}", savedUser.getEmail());
        return userMapper.registerResponseDto(savedUser);
    }

    /**
     * Login dell'utente
     *
     * <p> Il metodo esegue i seguenti step: </p>
     * <ul>
     *     <li> Crea un oggetto autenticazione con i dati inviati nel DTO </li>
     *     <li> Se i dati sono corretti, verifico che l'utente non si sia già registrato con n altro Provider </li>
     *     <li> Popolo il SecurityContextHolder e genero l' access e refresh token </li>
     *     <li> Salvo in refresh token in un Cookie accessibile solo al path /auth/refresh </li>
     * </ul>
     * @param request DTO con i dati per eseguire il login
     * @exception BadCredentialsException se le credenziali sono errate
     * @exception WrongAuthProvider se il provider non è quello Locale
     * @return un DTO con i dati del login
     */
    public LoginResponseDto login(LoginRequestDto request, HttpServletResponse response) {
        log.info("[LOGIN] Login in esecuzione per {}", request.getEmail());

        Authentication auth;
        try {
            auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(), request.getPassword()
            ));
        }  catch (BadCredentialsException ex) {
            log.warn("[LOGIN] Fallito per email {}: {}", request.getEmail(), ex.getMessage());
            throw ex;
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        if (!userDetails.getProvider().equals(AuthProvider.LOCALE)){
            SecurityContextHolder.clearContext();

            log.warn("[LOGIN] Fallito per email {}, accesso già eseguito con il provider {}",
                    userDetails.getEmail(), userDetails.getProvider());
            throw new WrongAuthProvider("Errore, esegui l'accesso attraverso il provider con cui ti sei registrato");
        }
        SecurityContextHolder.getContext().setAuthentication(auth);

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

        refreshTokenRepository.save(RefreshToken.builder()
                        .userId(userDetails.getId())
                        .refreshToken(refreshToken)
                        .expiresAt(LocalDateTime.now().plusDays(7))
                        .revoked(false)
                .build());

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);  //js non può leggerlo
        //refreshTokenCookie.setSecure(true);  solo HTTPS
        refreshTokenCookie.setPath("/api/auth/refresh");
        refreshTokenCookie.setMaxAge(Math.toIntExact(longExpiration));

        response.addCookie(refreshTokenCookie);

        log.info("[LOGIN] Refresh token salvato {}", refreshToken);
        log.info("[LOGIN] Login avvenuto con successo per {}", request.getEmail());
        return userMapper.loginResponseDto(userDetails, accessToken);
    }

    /**
     * <p> Il metodo esegue i seguenti step: </p>
     * <ul>
     *     <li> Ottengo i cookies dalla richiesta, verificando che siano presenti </li>
     *     <li> Ottengo il refreshToken dal cookie, e controllo che sia presente </li>
     *     <li> Ottengo l'id dell' utente dal token, visto che non è più salvato nel SecurityContext </li>
     *     <li> Ottengo l'oggetto refreshToken dal db con l'userId dell'utente e il token </li>
     *     <li> In questo modo verifico che non sia expire, in tal caso setto la sua data di expiration </li>
     *     <li> Genero un nuovo access token attraverso i dati dell'utente, e lo invio nella response </li>
     * </ul>
     * @param request
     * @return Access token
     */
    @Transactional
    public Map<String, String> refreshToken(HttpServletRequest request) {
        log.info("[REFRESH TOKEN] Refresh token ");

        Cookie[] cookies = request.getCookies();

        if (cookies == null){
            log.error("[REFRESH TOKEN] Nessun Cookie presente");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Nessun cookie trovato");
        }

        String refreshToken = "";
        for (Cookie cookie : cookies){
            if ("refreshToken".equals(cookie.getName())){
                refreshToken = cookie.getValue();
            }
            break;
        }

        if (refreshToken == null || refreshToken.isEmpty()) {
            log.error("[REFRESH TOKEN] Nessun refresh token presente nei cookies");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Nessun token trovato nel cookie");
        }

        /// Recupero utente sotto forma di UserDetailsImpl
        String userEmail = jwtService.estraiEmail(refreshToken);
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(userEmail);

        RefreshToken savedRefreshToken = refreshTokenRepository.findByRefreshTokenAndUserId(refreshToken, userDetails.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Nessun token trovato"));

        if (savedRefreshToken.getExpiresAt().isBefore(LocalDateTime.now())){
            savedRefreshToken.setExpiresAt(LocalDateTime.now());
            savedRefreshToken.setRevoked(true);
            refreshTokenRepository.save(savedRefreshToken);

            log.error("[REFRESH TOKEN] Refresh token scaduto {}", refreshToken);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token scaduto. Accedi per utilizzare l'applicazione");
        }

        String newAccessToken = jwtService.generateToken(
                false,
                userDetails.getId(),
                userDetails.getEmail(),
                userDetails.getAuthorities(),
                userDetails.getProvider()
        );

        log.info("[REFRESH TOKEN] Access token generato con successo per user {}", userDetails.getId());
        HashMap<String, String> response = new HashMap<>();
        response.put("Access token", newAccessToken);
        return response;
    }
}
