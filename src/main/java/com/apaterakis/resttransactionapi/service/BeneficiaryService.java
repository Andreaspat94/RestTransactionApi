package com.apaterakis.resttransactionapi.service;

import com.apaterakis.resttransactionapi.model.Beneficiary;
import com.apaterakis.resttransactionapi.repository.BeneficiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;

    @Autowired
    public BeneficiaryService(BeneficiaryRepository beneficiaryRepository) {
        this.beneficiaryRepository = beneficiaryRepository;
    }

    public Optional<Beneficiary> findById(Long id) {
        return beneficiaryRepository.findById(id);
    }
}
