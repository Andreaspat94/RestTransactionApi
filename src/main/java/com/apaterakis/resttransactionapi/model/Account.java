package com.apaterakis.resttransactionapi.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name="accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "beneficiary_id",
            referencedColumnName = "beneficiaryId",
            foreignKey = @ForeignKey(name="FK_ACCOUNT_BENEFICIARY"))
    @JsonIgnoreProperties("accounts")
    private Beneficiary beneficiary;

    @Column(nullable = false)
    private BigDecimal balance;


    public Account(Beneficiary beneficiary, BigDecimal balance) {
        this.beneficiary = beneficiary;
        this.balance = balance;
    }
}
