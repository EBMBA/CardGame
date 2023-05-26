package com.example.common.Exception;

import java.util.function.Supplier;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "User Already Exists")
public class UserAlreadyExistsException extends Exception {
    private static final long serialVersionUID = 1L;

    public UserAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }

    public static Supplier<UserAlreadyExistsException> of(String errorMessage) {
        return () -> new UserAlreadyExistsException(errorMessage);
    }
}
