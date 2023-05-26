package com.example.common.Exception;

import java.util.function.Supplier;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Wallet Not Found")
public class WalletNotFoundException extends Exception{
    private static final long serialVersionUID = 1L;

    public WalletNotFoundException(String errorMessage) {
        super(errorMessage);
    }

    public static Supplier<WalletNotFoundException> of(String errorMessage) {
        return () -> new WalletNotFoundException(errorMessage);
    }
}
