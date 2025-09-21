package com.fit_planner_ai.app.service;

import com.fit_planner_ai.app.dto.LoginRequestDto;
import com.fit_planner_ai.app.dto.LoginResponseDto;
import com.fit_planner_ai.app.dto.RegisterRequestDto;
import com.fit_planner_ai.app.dto.RegisterResponseDto;
import com.fit_planner_ai.app.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;

    public RegisterResponseDto register(RegisterRequestDto request) {
    }

    public LoginResponseDto login(LoginRequestDto request) {
    }
}
