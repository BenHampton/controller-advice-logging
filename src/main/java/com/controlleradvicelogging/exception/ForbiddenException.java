package com.controlleradvicelogging.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 4340661465577589282L;

    public ForbiddenException(String message) {
        super(message);
    }
}
