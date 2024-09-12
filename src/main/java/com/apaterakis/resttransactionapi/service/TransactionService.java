package com.apaterakis.resttransactionapi.service;

import com.apaterakis.resttransactionapi.exception.InsufficientBalanceException;
import com.apaterakis.resttransactionapi.model.Account;
import com.apaterakis.resttransactionapi.model.Transaction;
import com.apaterakis.resttransactionapi.model.TransactionType;
import com.apaterakis.resttransactionapi.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * This method is only for loading .csv data
     */
    public Transaction makeTransaction(Transaction transaction) {
        Account account = transaction.getAccount();
//        transaction.setDate(LocalDate.now());
//        transaction.getFormattedDate();
        if (transaction.getType().equals(TransactionType.WITHDRAWAL)) {
            BigDecimal result = account.getBalance().subtract(transaction.getAmount());

            if (result.compareTo(BigDecimal.ZERO) < 0) {
                throw new InsufficientBalanceException("Insufficient funds to complete the transaction from account with id: " + account.getAccountId());
            }
        }
        return transactionRepository.save(transaction);
    }
}
