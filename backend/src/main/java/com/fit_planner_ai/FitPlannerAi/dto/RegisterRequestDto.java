package com.fit_planner_ai.FitPlannerAi.dto;

import com.fit_planner_ai.FitPlannerAi.model.Roles;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequestDto {

    @NotBlank(message = "Il nome utente è obbligatorio")
    @Size(min = 3, max = 20, message = "Il nome utente deve essere tra 3 e 20 caratteri")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Il nome utente può contenere solo lettere, numeri e underscore")
    private String userName;

    @NotBlank(message = "L'email non può essere vuota")
    @Email(message = "Formato email non valido")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Email non valida: deve contenere un dominio valido"
    )
    private String email;

    @NotNull(message = "La password non può essere vuota")
    @Size(min = 6, message = "La password deve essere lunga almeno 6 caratteri")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$",
            message = "La password deve contenere almeno una maiuscola, una minuscola, un numero e un carattere speciale")
    private String password;

    private Set<Roles> roles;
}
