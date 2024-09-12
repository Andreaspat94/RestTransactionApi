package com.apaterakis.resttransactionapi.exception;

public class BadRequestException extends RuntimeException {
    private int status;
    private boolean successful;

    public BadRequestException() {}

    public BadRequestException(int status, String message) {
        super(message);
        this.status = status;
    }

}
