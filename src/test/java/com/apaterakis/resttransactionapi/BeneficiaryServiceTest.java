package com.apaterakis.resttransactionapi;

import com.apaterakis.resttransactionapi.model.Account;
import com.apaterakis.resttransactionapi.model.Beneficiary;
import com.apaterakis.resttransactionapi.repository.BeneficiaryRepository;
import com.apaterakis.resttransactionapi.service.BeneficiaryService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BeneficiaryServiceTest {

    @Mock
    private BeneficiaryRepository repository;

    @InjectMocks
    private BeneficiaryService service;

    private Beneficiary beneficiary;
    private Account account;

    @BeforeEach
    void setUp() {
        beneficiary = new Beneficiary("firstName", "lastName");
        account = new Account(beneficiary, new BigDecimal("5000.00"));
        beneficiary.setAccounts(List.of(account));
    }

    @Test
    void findById() {
        when(repository.findById(any())).thenReturn(Optional.of(beneficiary));
        Optional<Beneficiary> result = service.findById(1L);
        verify(repository).findById(any());

        assertEquals(beneficiary, result.get());
    }

    @Test
    void findByIdNotFound() {
        when(repository.findById(any())).thenReturn(Optional.empty());
        Optional<Beneficiary> result = service.findById(1L);
        verify(repository).findById(any());

        assertEquals(Optional.empty(), result);
    }
}
