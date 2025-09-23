package com.fit_planner_ai.app.service;

import com.fit_planner_ai.app.dto.UserDto;
import com.fit_planner_ai.app.model.User;
import com.fit_planner_ai.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto getUser() {

    }


}
