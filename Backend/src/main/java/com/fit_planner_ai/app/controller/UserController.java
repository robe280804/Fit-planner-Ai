package com.fit_planner_ai.app.controller;

import com.fit_planner_ai.app.dto.CreateUserDto;
import com.fit_planner_ai.app.dto.UserDto;
import com.fit_planner_ai.app.dto.UserUpdateDto;
import com.fit_planner_ai.app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /// GET USER (ottieni un utente)
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/")
    public ResponseEntity<UserDto> getUser(){
        return ResponseEntity.ok(userService.getUser());
    }

    /// GET USERS (ottieni tutti gli utenti)
    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getUsers(){
        return ResponseEntity.ok(userService.getUsers());
    }

    /// POST USER (aggiorna un utente)
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/update")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserUpdateDto request){
        return ResponseEntity.ok(userService.update(request));
    }
    /// POST (crea un utente, ruolo: ADMIN)
    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserDto request){
        return ResponseEntity.ok(userService.create(request));
    }
    /// PUT USER (aggiorna un campo dell'utente)

    /// DELETE USER (elimina un utente, ruolo: ADMIN)
    @DeleteMapping("/{userId}")
    public ResponseEntity<Boolean> delete(@PathVariable UUID userId){
        return ResponseEntity.ok(userService.delete(userId));
    }
}
