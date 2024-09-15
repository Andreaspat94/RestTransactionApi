package com.apaterakis.resttransactionapi.service;

import com.apaterakis.resttransactionapi.model.Account;
import com.apaterakis.resttransactionapi.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
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

//    public List<Account> findAccountsByBeneficiaryId(Long id) {
//        return accountRepository.findAccountsByBeneficiaryId(id);
//    }

}
