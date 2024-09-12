package com.apaterakis.resttransactionapi.model;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ErrorResponse {
    private final int status;
    private final String message;
    private boolean successful;
}


