package com.apaterakis.resttransactionapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BeneficiaryUpdateRequest {
    private final String firstName;
    private final String lastName;
}
