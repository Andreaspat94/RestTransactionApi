package com.apaterakis.resttransactionapi.service;

import com.apaterakis.resttransactionapi.exception.InsufficientBalanceException;
import com.apaterakis.resttransactionapi.exception.NotFoundException;
import com.apaterakis.resttransactionapi.model.*;
import com.apaterakis.resttransactionapi.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private BeneficiaryService beneficiaryService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private TransactionService transactionService;

    List<Transaction> transactions;
    private Transaction transaction1;
    private Transaction transaction2;
    private Transaction transaction3;

    private Beneficiary beneficiary;
    private Account account1;
    private Account account2;
    private TransactionRequest request;

    @BeforeEach
    void setUp() {
        beneficiary = new Beneficiary("firstName", "lastName");
        account1 = new Account(beneficiary, new BigDecimal("5000.00"));
        account2 = new Account(beneficiary, new BigDecimal("5000.00"));
        beneficiary.setAccounts(List.of(account1, account2));

        transactions = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        LocalDate date = LocalDate.parse("01/10/23", formatter);
        transaction1 = new Transaction(1L, new BigDecimal(200), TransactionType.WITHDRAWAL, date);
        transaction2 = new Transaction(1L, new BigDecimal(100), TransactionType.DEPOSIT, date);
        transaction3 = new Transaction(1L, new BigDecimal(300), TransactionType.WITHDRAWAL, date);

        request = new TransactionRequest(1802L, new BigDecimal(100), TransactionType.WITHDRAWAL);
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

    @Test
    void createTransaction() {
        when(accountService.findById(any())).thenReturn(Optional.of((account1)));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction3);

        Transaction result = transactionService.createTransaction(request);

        verify(accountService).save(account1);
        verify(transactionRepository).save(any(Transaction.class));

        assertNotNull(result);
        assertEquals(transaction3, result);

    }

    @Test
    void createTransactionWhenAccountNotFound() {
        when(accountService.findById(1802L)).thenReturn(Optional.empty());

        final NotFoundException exc = assertThrows(NotFoundException.class,
                () -> transactionService.createTransaction(request));

        verify(accountService).findById(1802L);

        String actualMessage = exc.getMessage();
        String expectedMessage = "Account with id: 1802 has not found.";

        assertThrows(NotFoundException.class, () -> {
            transactionService.createTransaction(request);
        });

        assertEquals(404, exc.getStatus());
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void proceedTransaction() {

        when(accountService.save(any())).thenReturn(account1);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction1);

        Transaction response = transactionService.proceedTransaction(transaction1, account1);

        verify(accountService).save(any());
        verify(transactionRepository).save(any(Transaction.class));

        assertEquals(transaction1, response);
        assertNotNull(response);
    }

    @Test
    void proceedTransactionThrowInsufficientBalance() {
        // Set a low balance for the account
        account1.setBalance(new BigDecimal("50.00"));

        final InsufficientBalanceException exc = assertThrows(InsufficientBalanceException.class,
                () -> transactionService.proceedTransaction(transaction1, account1));

        verify(accountService, times(0)).save(account1);  // No save should be triggered
        verify(transactionRepository, times(0)).save(transaction1);

        String actualMessage = exc.getMessage();
        String expectedMessage = "Insufficient funds to complete the transaction.";

        assertEquals(403, exc.getStatus());
        assertThrows(InsufficientBalanceException.class, () -> {
            transactionService.proceedTransaction(transaction1, account1);
        });
        assertTrue(actualMessage.contains(expectedMessage));
    }
}

