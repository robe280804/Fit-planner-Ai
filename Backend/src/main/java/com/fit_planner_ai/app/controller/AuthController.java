package com.fit_planner_ai.app.controller;


import com.fit_planner_ai.app.dto.LoginRequestDto;
import com.fit_planner_ai.app.dto.LoginResponseDto;
import com.fit_planner_ai.app.dto.RegisterRequestDto;
import com.fit_planner_ai.app.dto.RegisterResponseDto;
import com.fit_planner_ai.app.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody @Valid RegisterRequestDto request){
        return ResponseEntity.status(201).body(authService.register(request));
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto request){
        return ResponseEntity.ok(authService.login(request));
    }


}
