package com.apaterakis.resttransactionapi.service;

import com.apaterakis.resttransactionapi.model.Account;
import com.apaterakis.resttransactionapi.model.Beneficiary;
import com.apaterakis.resttransactionapi.model.BeneficiaryRequest;
import com.apaterakis.resttransactionapi.model.BeneficiaryUpdateRequest;
import com.apaterakis.resttransactionapi.repository.AccountRepository;
import com.apaterakis.resttransactionapi.repository.BeneficiaryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository repository;

    @InjectMocks
    private AccountService service;

    private Beneficiary beneficiary;
    private Account account;

    @BeforeEach
    void setUp() {
        beneficiary = new Beneficiary("firstName", "lastName");
        account = new Account(beneficiary, new BigDecimal("5000.00"));
    }

    @Test
    void findById() {
        when(repository.findById(any())).thenReturn(Optional.of(account));
        Optional<Account> result = service.findById(1L);
        verify(repository).findById(any());

        assertEquals(account, result.get());
    }

    @Test
    void findByIdNotFound() {
        when(repository.findById(any())).thenReturn(Optional.empty());
        Optional<Account> result = service.findById(1L);
        verify(repository).findById(any());

        assertEquals(Optional.empty(), result);
    }
}
