package com.ProfitMint.login.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a session is invalid or expired.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidSessionException extends RuntimeException {

    public InvalidSessionException() {
        super("Session is invalid or expired");
    }

    public InvalidSessionException(String message) {
        super(message);
    }
}

