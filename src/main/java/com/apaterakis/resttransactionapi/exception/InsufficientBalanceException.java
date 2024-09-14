package com.apaterakis.resttransactionapi.exception;

import org.springframework.http.HttpStatus;

public class InsufficientBalanceException extends RuntimeException{
    private int status;
    private boolean successful;

    public InsufficientBalanceException() {
    }

    public InsufficientBalanceException(String message) {
        super(message);
        this.status = HttpStatus.FORBIDDEN.value();
    }
}
