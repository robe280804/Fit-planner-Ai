package com.fit_planner_ai.FitPlannerAi.controller;

import com.fit_planner_ai.FitPlannerAi.dto.*;
import com.fit_planner_ai.FitPlannerAi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @PreAuthorize("hasAnyRole('USER', 'TRAINER', 'ADMIN')")
    @PatchMapping("/username")
    public ResponseEntity<UpdateUserDto> updateUserName(@RequestBody @Valid UserNameUpdateRequestDto request){
        return ResponseEntity.ok(userService.updateUserName(request));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/{trainerId}/get-trainer")
    public ResponseEntity<GetTrainerDto> getTrainer(@PathVariable UUID trainerId){
        return ResponseEntity.ok(userService.getTrainer(trainerId));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'TRAINER')")
    @PatchMapping("/email")
    public ResponseEntity<UpdateUserDto> updateEmail(@RequestBody @Valid EmailUpdateRequestDto request){
        return ResponseEntity.ok(userService.updateEmail(request));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'TRAINER')")
    @GetMapping("/")
    public ResponseEntity<UserDto> getUser(){
        return ResponseEntity.ok(userService.getUser());
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'TRAINER')")
    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUser(){
        return ResponseEntity.ok(userService.getAllUser());
    }



}
