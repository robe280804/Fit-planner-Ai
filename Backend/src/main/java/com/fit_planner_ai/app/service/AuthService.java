package com.fit_planner_ai.app.service;

import com.fit_planner_ai.app.dto.LoginRequestDto;
import com.fit_planner_ai.app.dto.LoginResponseDto;
import com.fit_planner_ai.app.dto.RegisterRequestDto;
import com.fit_planner_ai.app.dto.RegisterResponseDto;
import com.fit_planner_ai.app.enums.AuthProvider;
import com.fit_planner_ai.app.exception.UserAlredyRegister;
import com.fit_planner_ai.app.exception.WrongAuthProvider;
import com.fit_planner_ai.app.mapper.UserMapper;
import com.fit_planner_ai.app.model.User;
import com.fit_planner_ai.app.repository.UserRepository;
import com.fit_planner_ai.app.security.jwt.JwtService;
import com.fit_planner_ai.app.security.model.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

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
     *     <li> Popolo il SecurityContextHolder e genero il token </li>
     * </ul>
     * @param request DTO con i dati per eseguire il login
     * @exception BadCredentialsException se le credenziali sono errate
     * @exception WrongAuthProvider se il provider non è quello Locale
     * @return un DTO con i dati del login
     */
    public LoginResponseDto login(LoginRequestDto request) {
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

        String token = jwtService.generateToken(
                userDetails.getId(),
                userDetails.getEmail(),
                userDetails.getAuthorities(),
                userDetails.getProvider());

        log.info("[LOGIN] Login avvenuto con successo per {}", request.getEmail());
        return userMapper.loginResponseDto(userDetails, token);
    }
}
