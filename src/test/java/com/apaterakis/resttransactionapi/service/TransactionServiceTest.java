package com.apaterakis.resttransactionapi.service;

import com.apaterakis.resttransactionapi.model.Account;
import com.apaterakis.resttransactionapi.model.Beneficiary;
import com.apaterakis.resttransactionapi.model.Transaction;
import com.apaterakis.resttransactionapi.model.TransactionType;
import com.apaterakis.resttransactionapi.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private BeneficiaryService beneficiaryService;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    List<Transaction> transactions;
    private Transaction transaction1;
    private Transaction transaction2;
    private Transaction transaction3;

    private Beneficiary beneficiary;
    private Account account1;
    private Account account2;

    @BeforeEach
    void setUp() {
        beneficiary = new Beneficiary("firstName", "lastName");
        account1 = new Account(beneficiary, new BigDecimal("5000.00"));
        account2 = new Account(beneficiary, new BigDecimal("5000.00"));
        beneficiary.setAccounts(List.of(account1, account2));

        transactions = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        LocalDate date = LocalDate.parse("01/10/23", formatter);
        transaction1 = new Transaction(1L,new BigDecimal(200), TransactionType.WITHDRAWAL, date);
        transaction2 = new Transaction(1L,new BigDecimal(100), TransactionType.DEPOSIT, date);
        transaction3 = new Transaction(1L,new BigDecimal(300), TransactionType.WITHDRAWAL, date);
    }

    @Test
    void findByBeneficiaryId() {
        when(beneficiaryService.findById(any())).thenReturn(Optional.of(beneficiary));
        when(transactionRepository.findAllByAccountId(any())).thenReturn(transactions);

        List<Transaction> result = transactionService.findByBeneficiaryId(1L);

        verify(beneficiaryService).findById(any());
        verify(transactionRepository, times(2)).findAllByAccountId(any());

        assertEquals(transactions, result);
    }

    @Test
    void findByBeneficiaryIdNotFound() {
        when(beneficiaryService.findById(any())).thenReturn(Optional.empty());
        List<Transaction> result = transactionService.findByBeneficiaryId(1L);
        verify(beneficiaryService).findById(any());

        assertEquals(List.of(), result);
    }

    @Test
    void findLargestTransaction() {
        transactions.add(transaction3);
        Transaction result = transactionService.findLargestTransaction(transactions);

        assertEquals(transaction3, result);
    }
}

