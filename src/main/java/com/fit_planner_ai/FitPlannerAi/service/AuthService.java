package com.fit_planner_ai.FitPlannerAi.service;

import com.fit_planner_ai.FitPlannerAi.dto.LoginRequestDto;
import com.fit_planner_ai.FitPlannerAi.dto.LoginResponseDto;
import com.fit_planner_ai.FitPlannerAi.dto.RegisterRequestDto;
import com.fit_planner_ai.FitPlannerAi.dto.RegisterResponseDto;
import com.fit_planner_ai.FitPlannerAi.exception.UserAlredyRegisterEx;
import com.fit_planner_ai.FitPlannerAi.mapper.AuthMapper;
import com.fit_planner_ai.FitPlannerAi.model.*;
import com.fit_planner_ai.FitPlannerAi.repository.TrainerRepository;
import com.fit_planner_ai.FitPlannerAi.repository.UserRepository;
import com.fit_planner_ai.FitPlannerAi.security.model.UserDetailsImpl;
import com.fit_planner_ai.FitPlannerAi.security.service.JwtService;
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

import java.util.Optional;

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

    public RegisterResponseDto register(RegisterRequestDto request) {
        log.info("[REGISTER] Registrazione in esecuzione per {} ", request.getEmail());

        BaseUser user;
        if (request.getRoles().contains(Roles.TRAINER)){
            //Registro l'utente come trainer
            if (trainerRepository.existsByEmail(request.getEmail())){
                log.warn("[REGISTER] Utente {} già registrato ", request.getEmail());
                throw new UserAlredyRegisterEx("Utente già registrato nel sistema");
            }
            user = trainerRepository.save(Trainer.builder()
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
                    .email(request.getEmail())
                    .password(encoder.encode(request.getPassword()))
                    .roles(request.getRoles())
                    .provider(AuthProvider.LOCALE)
                    .build());
        }
        log.info("[REGISTER] Registrazione avvenuta con successo per {}", user.getEmail());
        return authMapper.registerResponseDto(user);
    }

    public LoginResponseDto login(LoginRequestDto request) {
        //Qui metto la logica per vedere se il provider è locale o esterno

        Authentication auth;
        try {
            auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(), request.getPassword()
            ));
        }  catch (BadCredentialsException ex) {
            log.warn("[LOGIN] Fallito per email {}: {}", request.getEmail(), ex.getMessage());
            throw ex;
        }
        SecurityContextHolder.getContext().setAuthentication(auth);
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        String token = jwtService.generateToken(
                userDetails.getId(),
                userDetails.getEmail(),
                userDetails.getAuthorities(),
                userDetails.getProvider());

        return authMapper.loginResponseDto(userDetails, token);
    }
}
