package com.apaterakis.resttransactionapi.service;

import com.apaterakis.resttransactionapi.model.Account;
import com.apaterakis.resttransactionapi.model.Beneficiary;
import com.apaterakis.resttransactionapi.model.BeneficiaryRequest;
import com.apaterakis.resttransactionapi.model.BeneficiaryUpdateRequest;
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
public class BeneficiaryServiceTest {

    @Mock
    private BeneficiaryRepository repository;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private BeneficiaryService service;

    private Beneficiary beneficiary;
    private Account account;
    private BeneficiaryRequest requestBody;
    private BeneficiaryUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        beneficiary = new Beneficiary("firstName", "lastName");
        account = new Account(beneficiary, new BigDecimal("5000.00"));
        beneficiary.setAccounts(List.of(account));
        requestBody = new BeneficiaryRequest(beneficiary.getFirstName(), beneficiary.getLastName(), new BigDecimal(5000));
        updateRequest = new BeneficiaryUpdateRequest("UpdatedName", "UpdatedLastName");
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

    @Test
    void createBeneficiary() {
        Beneficiary newBeneficiary = new Beneficiary(
                requestBody.getFirstName(),
                requestBody.getLastName()
        );
        Account newAccount = new Account(newBeneficiary,new BigDecimal(5000));

        when(repository.save(any(Beneficiary.class))).thenReturn(newBeneficiary);
        when(accountService.save(any(Account.class))).thenReturn(newAccount);

        Beneficiary result = service.createBeneficiary(requestBody);

        verify(repository, times(2)).save(any(Beneficiary.class));
        verify(accountService).save(any(Account.class));

        assertEquals(newBeneficiary, result);
    }

    @Test
    void updateBeneficiary() {
        Beneficiary updatedBeneficiary = new Beneficiary(updateRequest.getFirstName(), updateRequest.getLastName(), beneficiary.getAccounts());
        when(repository.save(any(Beneficiary.class))).thenReturn(updatedBeneficiary);

        Beneficiary result = service.updateBeneficiary(beneficiary, updateRequest);
        verify(repository).save(any(Beneficiary.class));

        assertEquals(updatedBeneficiary, result);
    }

    @Test
    void deleteBeneficiary() {
        service.deleteBeneficiary(1L);
        verify(repository,times(1)).deleteById(1L);
    }
}
