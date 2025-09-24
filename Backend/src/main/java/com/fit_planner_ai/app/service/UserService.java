package com.fit_planner_ai.app.service;

import com.fit_planner_ai.app.dto.CreateUserDto;
import com.fit_planner_ai.app.dto.UserDto;
import com.fit_planner_ai.app.dto.UserUpdateDto;
import com.fit_planner_ai.app.exception.UserNotFound;
import com.fit_planner_ai.app.mapper.UserMapper;
import com.fit_planner_ai.app.model.User;
import com.fit_planner_ai.app.repository.UserRepository;
import com.fit_planner_ai.app.security.model.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;

    public UserDto getUser() {
        UUID userId = getUserAuthId();
        log.info("[GET USER] Metodo in eseguzione per user {}", userId);

        return userRepository.findById(userId).map(userMapper::userDto)
                .orElseThrow(() -> new UserNotFound("Utente non trovato"));
    }

    public List<UserDto> getUsers() {
        UUID userId = getUserAuthId();
        log.info("[GET USERS] Metodo in eseguzione per user {}", userId);

        return userRepository.findAll().stream()
                .map(userMapper::userDto)
                .collect(Collectors.toList());
    }

    public UserDto update(UserUpdateDto request) {
        UUID userId = getUserAuthId();
        log.info("[UPDATE USER] Metodo update in eseguzione per user {}", userId);

        User existUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFound("Utente non trovato"));

        existUser.setName(request.getName());
        existUser.setSurname(request.getSurname());
        existUser.setUsername(request.getUsername());

        User savedUser = userRepository.save(existUser);

        log.info("[UPDATE USER] Metodo andato a buon fine per {}", savedUser);
        return userMapper.userDto(savedUser);
    }

    public UserDto create( CreateUserDto request) {
        UUID adminId = getUserAuthId();
        log.info("[CREATE] Creazione nuovo utente da parte dell'admin {}", adminId);

        User newUser = User.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .provider(request.getProvider())
                .roles(request.getRoles())
                .build();

        User savedUser = userRepository.save(newUser);

        log.info("[CREATE] Creazione nuovo utente andata a buon fine: {}", savedUser);
        return userMapper.userDto(savedUser);
    }

    public Boolean delete(UUID userId) {
        UUID adminId = getUserAuthId();
        log.info("[DELETE] Eliminazione dell'utente {} da parte dell'admin {}", userId, adminId);

       if (!userRepository.existsById(userId)){
           return false;
       }
       userRepository.deleteById(userId);
       return true;
    }

    private static UUID  getUserAuthId() {
        UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }

}
