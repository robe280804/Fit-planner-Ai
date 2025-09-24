package com.fit_planner_ai.app.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCCredentialsEx(BadCredentialsException ex, WebRequest request){
        return generateResponse("Credenziali errate", ex, HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleArgumentNotValidEx(MethodArgumentNotValidException ex, WebRequest request){
        Map<String, Object> response = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                response.put(err.getField(), err.getDefaultMessage()));

        response.put("timestamps", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST);
        response.put("error", "Errore di validazione");
        response.put("message", ex.getMessage());
        response.put("path", extractPath(request));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlredyRegister.class)
    public ResponseEntity<Object> handleUserAlredyRegisterEx(UserAlredyRegister ex, WebRequest request){
        return generateResponse("Richiesta errata", ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundEx(UsernameNotFoundException ex, WebRequest request){
        return generateResponse("Errore interno", ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundEx(EntityNotFoundException ex, WebRequest request){
        return generateResponse("Errore interno", ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(WrongAuthProvider.class)
    public ResponseEntity<Object> wrongAuthProviderEx(WrongAuthProvider ex, WebRequest request){
        return generateResponse("Authentication provider errato", ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<Object> wrongAuthProviderEx(UserNotFound ex, WebRequest request){
        return generateResponse("Errore interno", ex, HttpStatus.NOT_FOUND, request);
    }

    private static ResponseEntity<Object> generateResponse(
            String error, Exception ex, HttpStatus status, WebRequest request
    ){
        Map<String, Object> response = new HashMap<>();
        response.put("timestamps", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", ex.getMessage());
        response.put("path", extractPath(request));

        return new ResponseEntity<>(response, status);
    }

    private static String extractPath(WebRequest request){
        return request.getDescription(false).replace("uri=", "");
    }
}
