package com.apaterakis.resttransactionapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AccountRequest {

    private final Long beneficiaryId;
    private final BigDecimal balance;
}
