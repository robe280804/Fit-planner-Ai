package com.fit_planner_ai.app.mapper;

import com.fit_planner_ai.app.dto.LoginResponseDto;
import com.fit_planner_ai.app.dto.RegisterResponseDto;
import com.fit_planner_ai.app.dto.UserDto;
import com.fit_planner_ai.app.enums.Role;
import com.fit_planner_ai.app.model.User;
import com.fit_planner_ai.app.security.model.UserDetailsImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    public RegisterResponseDto registerResponseDto(User user){
        return RegisterResponseDto.builder()
                .message("Registrazione avvenuta con successo")
                .idUtente(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .provider(user.getProvider())
                .roles(user.getRoles())
                .build();
    }


    public LoginResponseDto loginResponseDto(UserDetailsImpl userDetails, String token) {
        return LoginResponseDto.builder()
                .message("Login effettuato con successo")
                .id(userDetails.getId())
                .email(userDetails.getEmail())
                .provider(userDetails.getProvider())
                .roles(userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .map(role -> Role.valueOf(role.replace("ROLE_", "")))
                        .collect(Collectors.toSet()))
                .token(token)
                .build();
    }

    public UserDto userDto(User user){
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .username(user.getUsername())
                .email(user.getEmail())
                .provider(user.getProvider())
                .roles(user.getRoles())
                .build();
    }
}
