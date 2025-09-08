package com.fit_planner_ai.FitPlannerAi.service;

import com.fit_planner_ai.FitPlannerAi.dto.RegisterRequestDto;
import com.fit_planner_ai.FitPlannerAi.dto.RegisterResponseDto;
import com.fit_planner_ai.FitPlannerAi.exception.UserAlredyRegisterEx;
import com.fit_planner_ai.FitPlannerAi.mapper.AuthMapper;
import com.fit_planner_ai.FitPlannerAi.model.*;
import com.fit_planner_ai.FitPlannerAi.repository.TrainerRepository;
import com.fit_planner_ai.FitPlannerAi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;
    private final PasswordEncoder encoder;
    private final AuthMapper authMapper;

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
}
