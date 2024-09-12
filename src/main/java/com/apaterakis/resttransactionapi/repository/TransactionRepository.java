package com.apaterakis.resttransactionapi.repository;

import com.apaterakis.resttransactionapi.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Query to find transactions for a beneficiary
    @Query("SELECT t from Transaction t WHERE t.account.beneficiary.beneficiaryId = :beneficiaryId")
    Optional<List<Transaction>> findByBeneficiaryId(@Param("beneficiaryId") Long beneficiaryId);
}
