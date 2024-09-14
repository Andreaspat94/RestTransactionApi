package com.apaterakis.resttransactionapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DuplicateBeneficiaryException extends RuntimeException {
    private int status;
    private boolean successful;

    public DuplicateBeneficiaryException() {}

    public DuplicateBeneficiaryException(String message) {
        super(message);
        this.status = HttpStatus.CONFLICT.value();
    }

}
