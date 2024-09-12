package com.apaterakis.resttransactionapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @ManyToOne
    @JoinColumn(name = "account_id",
            referencedColumnName = "accountId",
            foreignKey = @ForeignKey(name = "FK_TRANSACTION_ACCOUNT"))
    private Account account;

    @NotNull
    @Column(nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @NotNull
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yy");
    @Column(nullable = false)
    private LocalDate date;

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        return this.date.format(formatter);
    }

    public Transaction(Account account, BigDecimal amount, TransactionType type, LocalDate date) {
        this.account = account;
        this.amount = amount;
        this.type = type;
        this.date = date;
    }
}
