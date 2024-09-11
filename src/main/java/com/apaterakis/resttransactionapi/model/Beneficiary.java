package com.apaterakis.resttransactionapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Getter
@Setter
@ToString
//@ToString(exclude = "accounts") // avoid infinite recursion between account and beneficiary circular calls
@NoArgsConstructor
@Entity
@Table(name="beneficiaries")
public class Beneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long beneficiaryId;

    @NotNull
    @Column(nullable = false)
    private String firstName;

    @NotNull
    @Column(nullable = false)
    private String lastName;

    @OneToMany(mappedBy = "beneficiary")
    private List<Account> accounts;
//
//    @OneToMany(mappedBy = "")
//    private List<Transaction> transactions;

    public Beneficiary(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
