package com.apaterakis.resttransactionapi.repository;

import com.apaterakis.resttransactionapi.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a FROM Account a WHERE a.beneficiary.beneficiaryId = :beneficiaryId")
    Optional<List<Account>> findAccountsByBeneficiaryId(@Param("beneficiaryId") Long beneficiaryId);
}
