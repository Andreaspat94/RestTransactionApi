package com.apaterakis.resttransactionapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Response<T> {
    private int status;
    private String message;
    private T data;
    private boolean successful;
}
