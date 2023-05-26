package com.example.common.Exception;

import java.util.function.Supplier;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

// @Getter
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User Not Found")
public class UserNotFoundException extends Exception  {
    // private final HttpStatus httpStatus;

    // private final Object[] args ;

    // public UserNotFoundException(HttpStatus httpStatus, String errorCode, Object... args) {
    //     super(errorCode);
    //     this.httpStatus = httpStatus;
    //     this.args = args;
    // }

    // public static Supplier<UserNotFoundException> of(String errorCode, Object id) {
    //     return () -> new UserNotFoundException(HttpStatus.NOT_FOUND, errorCode, id);
    // }

    private static final long serialVersionUID = 1L;

    public UserNotFoundException(String errorMessage) {
        super(errorMessage);
    }

    public static Supplier<UserNotFoundException> of(String errorMessage) {
        return () -> new UserNotFoundException(errorMessage);
    }
}
