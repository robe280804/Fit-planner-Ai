package com.fit_planner_ai.app.exception;

public class WrongAuthProvider extends RuntimeException {
    public WrongAuthProvider(String message) {
        super(message);
    }
}
