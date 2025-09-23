package com.fit_planner_ai.app.controller;

import com.fit_planner_ai.app.dto.UserDto;
import com.fit_planner_ai.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /// GET USER (ottieni un utente)
    public ResponseEntity<UserDto> getUser(){
        return ResponseEntity.ok(userService.getUser());
    }


    /// GET USERS (ottieni tutti gli utenti)
    /// POST USER (aggiorna un utente)
    /// POST (crea un utente, ruolo: ADMIN)
    /// PUT USER (aggiorna un campo dell'utente)
    /// DELETE USER (elimina un utente, ruolo: ADMIN)
}
