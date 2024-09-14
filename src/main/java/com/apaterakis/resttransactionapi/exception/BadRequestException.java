package com.apaterakis.resttransactionapi.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends RuntimeException {
    private int status;
    private boolean successful;

    public BadRequestException() {}

    public BadRequestException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST.value();
    }

}
