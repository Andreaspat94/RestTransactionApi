package com.apaterakis.resttransactionapi.model;
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
    private Beneficiary beneficiary;

    @Column(columnDefinition = "int default 0")
    private BigDecimal balance;

    public Account(Beneficiary beneficiary, BigDecimal balance) {
        this.beneficiary = beneficiary;
        this.balance = balance;
    }
}
