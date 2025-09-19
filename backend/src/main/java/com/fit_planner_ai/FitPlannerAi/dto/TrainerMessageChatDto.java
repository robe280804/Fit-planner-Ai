package com.fit_planner_ai.FitPlannerAi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.UUID;

@Data
public class TrainerMessageChatDto {

    private UUID userId;

    @NotBlank(message = "L'email non può essere vuota")
    @Email(message = "Formato email non valido")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Email non valida: deve contenere un dominio valido")
    private String from;

    private UUID destinatarioId;

    @NotBlank(message = "L'email non può essere vuota")
    @Email(message = "Formato email non valido")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Email non valida: deve contenere un dominio valido")
    private String destinatario;

    @NotBlank(message = "Type non può essere vuoto")
    private String type;

    @NotNull(message = "Il contenuto del messaggio non può essere nullo")
    private String contenuto;  //es: "Ciao! Per creare la tua scheda ho bisogno di obiettivo, giorni disponibili, tipi di allenamento e richieste particolari."
}
