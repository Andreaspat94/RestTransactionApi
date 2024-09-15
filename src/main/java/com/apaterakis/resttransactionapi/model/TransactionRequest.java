package com.apaterakis.resttransactionapi.model;

import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TransactionRequest {
    private final Long accountId;
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero.")
    private final BigDecimal amount;
    private final TransactionType type;
}
