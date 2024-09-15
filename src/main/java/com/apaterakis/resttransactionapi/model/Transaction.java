package com.apaterakis.resttransactionapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name="transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long transactionId;

    @NotNull
    @Column(nullable = false)
    private Long accountId;

    @NotNull
    @Column(nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @NotNull
    @JsonIgnore
    @Column(nullable = false)
    private LocalDate date;

    @JsonProperty("date")
    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        return this.date.format(formatter);
    }

    public Transaction(Long accountId, BigDecimal amount, TransactionType type, LocalDate date) {
        this.accountId = accountId;
        this.amount = amount;
        this.type = type;
        this.date = date;
    }
}
