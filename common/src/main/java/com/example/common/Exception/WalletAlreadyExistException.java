package com.example.common.Exception;

import java.util.function.Supplier;

import org.springframework.http.HttpStatus; 
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Wallet Already Exists")
public class WalletAlreadyExistException extends Exception {
    private static final long serialVersionUID = 1L;

    public WalletAlreadyExistException(String errorMessage) {
        super(errorMessage);
    }

    public static Supplier<WalletAlreadyExistException> of(String errorMessage) {
        return () -> new WalletAlreadyExistException(errorMessage);
    }
}
