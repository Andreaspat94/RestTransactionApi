package com.apaterakis.resttransactionapi.repository;

import com.apaterakis.resttransactionapi.model.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    boolean existsByBeneficiaryId(Long id);
}
