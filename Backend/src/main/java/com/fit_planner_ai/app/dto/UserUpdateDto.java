package com.fit_planner_ai.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDto {

    @NotBlank(message = "Il nome utente è obbligatorio")
    @Size(min = 3, max = 20, message = "Il nome utente deve essere tra 3 e 20 caratteri")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Il nome utente può contenere solo lettere, numeri e underscore")
    private String name;

    @NotBlank(message = "Il cognome utente è obbligatorio")
    @Size(min = 3, max = 20, message = "Il cognome utente deve essere tra 3 e 20 caratteri")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Il cognome utente può contenere solo lettere, numeri e underscore")
    private String surname;

    @NotBlank(message = "Lo username è obbligatorio")
    @Size(min = 3, max = 20, message = "Lo username utente deve essere tra 3 e 20 caratteri")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Lo username utente può contenere solo lettere, numeri e underscore")
    private String username;
}
