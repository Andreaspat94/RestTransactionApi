package com.apaterakis.resttransactionapi.service;

import com.apaterakis.resttransactionapi.exception.NotFoundException;
import com.apaterakis.resttransactionapi.model.Account;
import com.apaterakis.resttransactionapi.model.Beneficiary;
import com.apaterakis.resttransactionapi.model.Transaction;
import com.apaterakis.resttransactionapi.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final BeneficiaryService beneficiaryService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, BeneficiaryService beneficiaryService) {
        this.transactionRepository = transactionRepository;
        this.beneficiaryService = beneficiaryService;
    }

    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<Transaction> findByBeneficiaryId(Long id) {
        Optional<Beneficiary> beneficiaryOptional = beneficiaryService.findById(id);
        List<Transaction> transactionList = new ArrayList<>();
        if (beneficiaryOptional.isPresent()) {
            Beneficiary beneficiary = beneficiaryOptional.get();
            List<Account> accounts = beneficiary.getAccounts();
//            if (accounts.isEmpty()) {
//                throw new NotFoundException("Beneficiary with id: " + id + " has not accounts");
//            }

            for (Account account : accounts) {
                List<Transaction> transactions = transactionRepository.findAllByAccountId(account.getAccountId());
                transactionList.addAll(transactions);
            }
        }

        return transactionList;
    }
}
