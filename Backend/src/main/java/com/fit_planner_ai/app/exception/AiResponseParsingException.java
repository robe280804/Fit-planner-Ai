package com.fit_planner_ai.app.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class AiResponseParsingException extends RuntimeException {
    public AiResponseParsingException(String message, JsonProcessingException e) {
        super(message);
    }
}
