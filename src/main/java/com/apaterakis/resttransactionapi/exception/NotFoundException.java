package com.apaterakis.resttransactionapi.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private int status;
    private boolean successful;

    public NotFoundException() {
    }

    public NotFoundException(int status, String message) {
        super(message);
        this.status = status;
    }

}
