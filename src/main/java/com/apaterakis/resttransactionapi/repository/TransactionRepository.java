package com.apaterakis.resttransactionapi.repository;

import com.apaterakis.resttransactionapi.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByAccountId(Long beneficiaryId);

    //!!OLD: Query to find transactions for a beneficiary
//    @Query("SELECT t FROM Transaction t WHERE t.account.beneficiary.beneficiaryId = :beneficiaryId ORDER BY t.date DESC")
//    List<Transaction> findByBeneficiaryId(@Param("beneficiaryId") Long beneficiaryId);


}
