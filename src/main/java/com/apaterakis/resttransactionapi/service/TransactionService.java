package com.apaterakis.resttransactionapi.service;

import com.apaterakis.resttransactionapi.model.Account;
import com.apaterakis.resttransactionapi.model.Beneficiary;
import com.apaterakis.resttransactionapi.model.Transaction;
import com.apaterakis.resttransactionapi.model.TransactionType;
import com.apaterakis.resttransactionapi.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
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
}
