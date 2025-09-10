package com.fit_planner_ai.FitPlannerAi.service;

import com.fit_planner_ai.FitPlannerAi.dto.LoginRequestDto;
import com.fit_planner_ai.FitPlannerAi.dto.LoginResponseDto;
import com.fit_planner_ai.FitPlannerAi.dto.RegisterRequestDto;
import com.fit_planner_ai.FitPlannerAi.dto.RegisterResponseDto;
import com.fit_planner_ai.FitPlannerAi.exception.UserAlredyRegisterEx;
import com.fit_planner_ai.FitPlannerAi.exception.WrongAuthProviderEx;
import com.fit_planner_ai.FitPlannerAi.mapper.AuthMapper;
import com.fit_planner_ai.FitPlannerAi.model.*;
import com.fit_planner_ai.FitPlannerAi.repository.TrainerRepository;
import com.fit_planner_ai.FitPlannerAi.repository.UserRepository;
import com.fit_planner_ai.FitPlannerAi.security.model.UserDetailsImpl;
import com.fit_planner_ai.FitPlannerAi.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;
    private final PasswordEncoder encoder;
    private final AuthMapper authMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    /**
     * Registtrazione utente
     *
     * <p> Il metodo esegue i seguenti step: </p>
     * <ul>
     *     <li> Controlla che nel DTO della richiesta sia presente i ruoli, altrimenti imposta USER </li>
     *     <li> Se il role = trainer, registra un allenatore altrimenti un utente normale o un admin </li>
     * </ul>
     * @param request DTO con i dati già validati per la registrazione
     * @exception UserAlredyRegisterEx se l'utente è già registrato
     * @return un DTO con i dati dell'utente registrato
     */
    public RegisterResponseDto register(RegisterRequestDto request) {
        log.info("[REGISTER] Registrazione in esecuzione per {} ", request.getEmail());

        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            request.setRoles(Set.of(Roles.USER));
        }

        BaseUser user;
        if (request.getRoles().contains(Roles.TRAINER)){
            //Registro l'utente come trainer
            if (trainerRepository.existsByEmail(request.getEmail())){
                log.warn("[REGISTER] Utente {} già registrato ", request.getEmail());
                throw new UserAlredyRegisterEx("Utente già registrato nel sistema");
            }
            user = trainerRepository.save(Trainer.builder()
                    .userName(request.getUserName())
                    .email(request.getEmail())
                    .password(encoder.encode(request.getPassword()))
                    .roles(request.getRoles())
                    .provider(AuthProvider.LOCALE)
                    .build());

        } else {
            //registro l'utente come user o admin
            if (userRepository.existsByEmail(request.getEmail())){
                log.warn("[REGISTER] Utente {} già registrato ", request.getEmail());
                throw new UserAlredyRegisterEx("Utente già registrato nel sistema");
            }
            user = userRepository.save(User.builder()
                    .userName(request.getUserName())
                    .email(request.getEmail())
                    .password(encoder.encode(request.getPassword()))
                    .roles(request.getRoles())
                    .provider(AuthProvider.LOCALE)
                    .build());
        }
        log.info("[REGISTER] Registrazione avvenuta con successo per {}", user.getEmail());
        return authMapper.registerResponseDto(user);
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
     * @exception WrongAuthProviderEx se il provider non è quello Locale
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
            throw new WrongAuthProviderEx("Errore, esegui l'accesso attraverso il provider con cui ti sei registrato");
        }
        SecurityContextHolder.getContext().setAuthentication(auth);

        String token = jwtService.generateToken(
                userDetails.getId(),
                userDetails.getEmail(),
                userDetails.getAuthorities(),
                userDetails.getProvider());

        log.info("[LOGIN] Login avvenuto con successo per {}", request.getEmail());
        return authMapper.loginResponseDto(userDetails, token);
    }
}
