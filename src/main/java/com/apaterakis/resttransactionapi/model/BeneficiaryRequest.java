package com.apaterakis.resttransactionapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BeneficiaryRequest {
    private final String firstName;
    private final String lastName;
    private final BigDecimal firstDeposit;
}
