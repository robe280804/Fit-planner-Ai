package com.fit_planner_ai.FitPlannerAi.service;

import com.fit_planner_ai.FitPlannerAi.dto.*;
import com.fit_planner_ai.FitPlannerAi.exception.AlredyTrainerClient;
import com.fit_planner_ai.FitPlannerAi.exception.TrainerAlreadyPresent;
import com.fit_planner_ai.FitPlannerAi.exception.TrainerIsFull;
import com.fit_planner_ai.FitPlannerAi.exception.UserAlredyRegisterEx;
import com.fit_planner_ai.FitPlannerAi.mapper.UserMapper;
import com.fit_planner_ai.FitPlannerAi.model.BaseUser;
import com.fit_planner_ai.FitPlannerAi.enums.Roles;
import com.fit_planner_ai.FitPlannerAi.model.Trainer;
import com.fit_planner_ai.FitPlannerAi.model.User;
import com.fit_planner_ai.FitPlannerAi.repository.BaseUserRepository;
import com.fit_planner_ai.FitPlannerAi.repository.TrainerRepository;
import com.fit_planner_ai.FitPlannerAi.repository.UserRepository;
import com.fit_planner_ai.FitPlannerAi.security.model.UserDetailsImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BaseUserRepository baseUserRepository;

    /**
     * Assegnazione dell'allenatore all'utente
     *
     * <p> Il metodo esegue i seguenti passaggi </p>
     * <ul>
     *     <li> Visualizzo che l'allenatore esista </li>
     *     <li> Ottengo l'utente attraverso authentication </li>
     *     <li> Controllo che l'utente non abbia già un allenatore</li>
     *     <li> Controllo che l'allenatore non abbia più di 3 clienti </li>
     *     <li> Controllo che l'utente non sia già un cliente dell'allenatore </li>
     *     <li> Salvo l'allenatore e il cliente </li>
     * </ul>
     * @param trainerId id dell'allenatore
     * @exception EntityNotFoundException se l'utente non viene trovato
     * @exception TrainerAlreadyPresent se l'utente ha già un allenatore
     * @exception TrainerIsFull se l'allenatore ha raggiunto il limite massimo di clienti (3)
     * @return un DTO con i dati dell'utente e dell'allenatore
     */
    @Transactional
    public GetTrainerDto getTrainer(UUID trainerId) {
        UUID userId = getAuthUserId();
        log.info("[GET TRAINER] Registrazione dell'alenatore per l'utente {}", userId);

        Trainer savedTrainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));

        if (user.getTrainer() != null){
            throw new TrainerAlreadyPresent("Hai già un allenatore con email " + savedTrainer.getEmail());
        }
        if (savedTrainer.getClients().size() >= 3){
            throw new TrainerIsFull("L'allenatore " + savedTrainer.getEmail() + " non ha più posti disponibili");
        }

        savedTrainer.getClients().forEach(client -> {
            if (client.getId().equals(userId)){
                throw new AlredyTrainerClient("Sei già un cliente di " + savedTrainer.getEmail());
            }
        });
        user.setTrainer(savedTrainer);
        savedTrainer.getClients().add(user);

        userRepository.save(user);
        trainerRepository.save(savedTrainer);

        log.info("[GET TRAINER] Registrazione dell'allenatore {} per l'utente {} avvenuta con successo",
                savedTrainer.getEmail(), user.getEmail());
        return userMapper.getTrainerDto(savedTrainer, user);
    }

    /**
     * Aggiornamento Username
     *
     * <p> Il metodo esegue i seguenti step: </p>
     * <ul>
     *     <li> Ottengo l'id dell'utente che esegue la richiesta </li>
     *     <li> Se l'utente esiste lo ottengo dal db e attraverso il setter imposto lo username </li>
     *     <li> Salvo l'utente nel db e ritorno un DTO </li>
     * </ul>
     * @param request DTO con l'username da aggiornare
     * @exception EntityNotFoundException se l'utente non esiste
     * @return DTO con i dati dell'utente
     */
    @Transactional
    public UpdateUserDto updateUserName(UserNameUpdateRequestDto request) {
        UUID userId = getAuthUserId();
        log.info("[UPDATE USERNAME] Aggiornamento username per {}", userId);

        BaseUser existUser = baseUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User non trovato"));
        existUser.setUserName(request.getUserName());

        BaseUser savedUser = baseUserRepository.save(existUser);

        log.info("[UPDATE USERNAME] Username aggiornato con successo per {}", userId);
        return userMapper.updateUserDto(savedUser);
    }

    /**
     * Aggiornamento email
     *
     * <p> Il metodo esegue i seguenti step: </p>
     * <ul>
     *     <li> Ottengo l'id dell'utente che esegue la richiesta </li>
     *     <li> Se l'utente esiste lo ottengo dal db e attraverso il setter imposto l'email </li>
     *     <li> Salvo l'utente nel db e ritorno un DTO </li>
     * </ul>
     * @param request DTO con l'username da aggiornare
     * @exception EntityNotFoundException se l'utente non esiste
     * @return DTO con i dati dell'utente
     */
    @Transactional
    public UpdateUserDto updateEmail(EmailUpdateRequestDto request) {
        UUID userId = getAuthUserId();
        log.info("[UPDATE EMAIL] Aggiornamento email per {}", userId);

        if (baseUserRepository.existsByEmail(request.getEmail())){
            throw new UserAlredyRegisterEx("Email già registrata");
        }
        BaseUser existUser = baseUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User non trovato"));

        existUser.setEmail(request.getEmail());

        BaseUser savedUser = baseUserRepository.save(existUser);

        log.info("[UPDATE EMAIL] Email aggiornato con successo per {}", userId);
        return userMapper.updateUserDto(savedUser);
    }

    /**
     * Ottenere un utente
     *
     * @exception EntityNotFoundException se l'utente non esiste
     * @return DTO con i dati dell'utente
     */
    public UserDto getUser() {
        UUID userId = getAuthUserId();

        BaseUser existUser = baseUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User non trovato"));

        return userMapper.userDto(existUser);
    }

    /**
     * Ottengo tutti gli utenti (esclusi admin)
     *
     * <p> Il metodo esegue i seguenti step: </p>
     * <ul>
     *     <li> Ottengo tutti gli utenti dal db filtrando per quelli con ruoli != ADMIN </li>
     *     <li> Ritorno una lista di DTO </li>
     * </ul>
     *
     * @return LIsta di DTO con i dati dell'utente
     */
    public List<UserDto> getAllUser() {
        UUID userId = getAuthUserId();

        return baseUserRepository.findAll().stream()
                .filter(user -> !user.getRoles().contains(Roles.ADMIN))
                .map(userMapper::userDto)
                .toList();
    }


    private UUID getAuthUserId(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getId();
    }
}
