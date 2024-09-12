package com.apaterakis.resttransactionapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Response {
    private int status;
    private String message;
    private Beneficiary beneficiary;
    private boolean successful;
}
