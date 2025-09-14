package com.fit_planner_ai.FitPlannerAi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class EmailUpdateRequestDto {

    @NotBlank(message = "L'email non può essere vuota")
    @Email(message = "Formato email non valido")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Email non valida: deve contenere un dominio valido"
    )
    private String email;
}
