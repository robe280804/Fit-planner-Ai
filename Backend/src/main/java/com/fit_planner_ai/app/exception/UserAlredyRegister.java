package com.fit_planner_ai.app.exception;

public class UserAlredyRegister extends RuntimeException {
    public UserAlredyRegister(String message) {
        super(message);
    }
}
