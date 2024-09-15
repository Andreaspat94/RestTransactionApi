package com.apaterakis.resttransactionapi.service;

import com.apaterakis.resttransactionapi.exception.InsufficientBalanceException;
import com.apaterakis.resttransactionapi.exception.NotFoundException;
import com.apaterakis.resttransactionapi.model.*;
import com.apaterakis.resttransactionapi.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final BeneficiaryService beneficiaryService;
    private final AccountService accountService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, BeneficiaryService beneficiaryService, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.beneficiaryService = beneficiaryService;
        this.accountService = accountService;
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

            for (Account account : accounts) {
                List<Transaction> transactions = transactionRepository.findAllByAccountId(account.getAccountId());
                transactionList.addAll(transactions);
            }
        }

        return transactionList;
    }

    public Transaction findLargestTransaction(List<Transaction> transactions) {

        List<Transaction> sortedByDateAndTypeList= transactions.stream()
                .filter(t -> t.getType() == TransactionType.WITHDRAWAL)
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .toList();

        LocalDate searchDate = sortedByDateAndTypeList.get(0).getDate();
        LocalDate startDate =  searchDate.withDayOfMonth(1);
        LocalDate endDate =  searchDate.withDayOfMonth(searchDate.lengthOfMonth());

        return sortedByDateAndTypeList.stream()
                .filter(t -> t.getDate().isAfter(startDate) && t.getDate().isBefore(endDate))
                .max(Comparator.comparing(Transaction::getAmount))
                .orElse(null);

    }

    @Transactional
    public Transaction createTransaction(TransactionRequest request) {
        Long accountId = request.getAccountId();
        Optional<Account> optionalAccount = accountService.findByIdWithLock(accountId);

        if (optionalAccount.isEmpty()) {
            throw new NotFoundException("Account with id: " + accountId + " has not found.");
        }
        Account account = optionalAccount.get();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        String dateString = LocalDate.now().format(formatter);
        LocalDate date = LocalDate.parse(dateString, formatter);
        Transaction transaction = new Transaction(accountId, request.getAmount(),request.getType(),date);

        return proceedTransaction(transaction, account);

    }

    public Transaction proceedTransaction(Transaction transaction, Account account) {
        BigDecimal newBalance;
        if (transaction.getType().equals(TransactionType.WITHDRAWAL)) {
            newBalance = account.getBalance().subtract(transaction.getAmount());

            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new InsufficientBalanceException("Insufficient funds to complete the transaction. ");
            }

        } else {
            newBalance = account.getBalance().add(transaction.getAmount());
        }
        account.setBalance(newBalance);
        accountService.save(account);

        return transactionRepository.save(transaction);
    }

    public boolean isTransactionTableEmpty() {
        return transactionRepository.count() == 0;
    }
}
