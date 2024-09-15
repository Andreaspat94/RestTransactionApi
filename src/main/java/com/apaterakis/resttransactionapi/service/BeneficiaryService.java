package com.apaterakis.resttransactionapi.service;

import com.apaterakis.resttransactionapi.model.Account;
import com.apaterakis.resttransactionapi.model.Beneficiary;
import com.apaterakis.resttransactionapi.model.BeneficiaryRequest;

import com.apaterakis.resttransactionapi.model.BeneficiaryUpdateRequest;
import com.apaterakis.resttransactionapi.repository.BeneficiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final AccountService accountService;

    @Autowired
    public BeneficiaryService(BeneficiaryRepository beneficiaryRepository, AccountService accountService) {
        this.beneficiaryRepository = beneficiaryRepository;
        this.accountService = accountService;
    }

    public Optional<Beneficiary> findById(Long id) {
        return beneficiaryRepository.findById(id);
    }

    public Beneficiary save(Beneficiary beneficiary) {
        return beneficiaryRepository.save(beneficiary);
    }

    public boolean existsByFirstNameAndLastName(String firstName, String lastName) {
        return beneficiaryRepository.existsByFirstNameAndLastName(firstName, lastName);
    }

    public Beneficiary createBeneficiary(BeneficiaryRequest request) {
        Beneficiary beneficiary = new Beneficiary(request.getFirstName(), request.getLastName());
        Beneficiary newBeneficiary = save(beneficiary);

        Account account = new Account(newBeneficiary, request.getFirstDeposit());
        accountService.save(account);

        newBeneficiary.setAccounts(new ArrayList<>(List.of(account)));
        return save(newBeneficiary);
    }

    public Beneficiary updateBeneficiary(Beneficiary beneficiary, BeneficiaryUpdateRequest beneficiaryRequest) {
        beneficiary.setFirstName(beneficiaryRequest.getFirstName());
        beneficiary.setLastName(beneficiaryRequest.getLastName());
        return save(beneficiary);
    }

    public void deleteBeneficiary(Long id) {
        beneficiaryRepository.deleteById(id);
    }

    public boolean isBeneficiaryTableEmpty() {
        return beneficiaryRepository.count() == 0;
    }
}
