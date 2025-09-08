package com.fit_planner_ai.FitPlannerAi.security.service;

import com.fit_planner_ai.FitPlannerAi.model.Trainer;
import com.fit_planner_ai.FitPlannerAi.model.User;
import com.fit_planner_ai.FitPlannerAi.repository.TrainerRepository;
import com.fit_planner_ai.FitPlannerAi.repository.UserRepository;
import com.fit_planner_ai.FitPlannerAi.security.model.UserDetailsImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        return userRepository.findByEmail(username)
                .map(UserDetailsImpl::new)
                .or(() -> trainerRepository.findByEmail(username)
                        .map(UserDetailsImpl::new))
                .orElseThrow(() -> new EntityNotFoundException("User non trovato"));
    }
}
