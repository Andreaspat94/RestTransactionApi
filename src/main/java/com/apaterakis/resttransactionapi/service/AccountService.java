package com.apaterakis.resttransactionapi.service;

import com.apaterakis.resttransactionapi.exception.NotFoundException;
import com.apaterakis.resttransactionapi.model.Account;
import com.apaterakis.resttransactionapi.model.AccountRequest;
import com.apaterakis.resttransactionapi.model.Beneficiary;
import com.apaterakis.resttransactionapi.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final BeneficiaryService beneficiaryService;

    @Autowired
    public AccountService(AccountRepository accountRepository, @Lazy BeneficiaryService beneficiaryService) {
        this.accountRepository = accountRepository;
        this.beneficiaryService = beneficiaryService;
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    public Optional<Account> findById(Long accountId) {
        return accountRepository.findById(accountId);
    }

    public boolean isAccountTableEmpty() {
        return accountRepository.count() == 0;
    }

    public Optional<Account> findByIdWithLock(Long accountId) {
        return accountRepository.findByIdWithLock(accountId);
    }

    public Account createAccount(AccountRequest request) {

        Optional<Beneficiary> optionalBeneficiary = beneficiaryService.findById(request.getBeneficiaryId());
        if (optionalBeneficiary.isEmpty()) {
            throw new NotFoundException("There is no beneficiary with this id.");
        }
        Beneficiary beneficiary = optionalBeneficiary.get();
        Account account = new Account(beneficiary, request.getBalance());
        return accountRepository.save(account);
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }
}
