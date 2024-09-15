package com.apaterakis.resttransactionapi.controller;
import com.apaterakis.resttransactionapi.exception.NotFoundException;
import com.apaterakis.resttransactionapi.model.*;
import com.apaterakis.resttransactionapi.service.AccountService;
import com.apaterakis.resttransactionapi.service.BeneficiaryService;
import com.apaterakis.resttransactionapi.service.LoadDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    @Mock
    AccountService service;

    @InjectMocks
    AccountController controller;

    private Beneficiary beneficiary;
    private Account account;
    private AccountRequest request;

    @BeforeEach
    void setUp() {
        beneficiary = new Beneficiary("firstName", "lastName");
        account = new Account(beneficiary, new BigDecimal("5000.00"));
        beneficiary.setAccounts(List.of(account));
        request = new AccountRequest(1802L, new BigDecimal(900));
    }

    @Test
    void findById() {
        when(service.findById(1L)).thenReturn(Optional.of(account));

        final ResponseEntity<Response> response = controller.findById(1L);
        verify(service).findById(any());

        assertEquals(account, response.getBody().getData());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void findByIdNotFound() {
        when(service.findById(any())).thenReturn(Optional.empty());
        final NotFoundException exc = assertThrows(NotFoundException.class,
                () -> controller.findById(1L));
        verify(service).findById(any());

        String actualMessage = exc.getMessage();
        String expectedMessage = "Account has not found.";

        assertEquals(404, exc.getStatus());
        assertThrows(NotFoundException.class, () -> controller.findById(1L));
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void createAccount() {
        Account newAccount = new Account(beneficiary, new BigDecimal(String.valueOf(request.getBalance())));
        when(service.createAccount(request)).thenReturn(newAccount);

        final ResponseEntity<Response> response = controller.createAccount(request);

        verify(service).createAccount(request);

        assertEquals(newAccount, response.getBody().getData());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void deleteBeneficiary() {
        when(service.findById(any())).thenReturn(Optional.of(account));

        final ResponseEntity<Response> response = controller.deleteAccount(1L);
        verify(service).findById(any());
        verify(service).deleteAccount(any());

        assertEquals("Account with id: 1 is deleted.", response.getBody().getMessage());;
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteBeneficiaryNotFound() {
        when(service.findById(any())).thenReturn(Optional.empty());
        final NotFoundException exc = assertThrows(NotFoundException.class,
                () -> controller.findById(1L));
        verify(service).findById(any());

        String actualMessage = exc.getMessage();
        String expectedMessage = "Account has not found.";

        assertEquals(404, exc.getStatus());
        assertThrows(NotFoundException.class, () -> controller.deleteAccount(1L));
        assertTrue(actualMessage.contains(expectedMessage));
    }
}

@WebMvcTest(AccountController.class)
@ExtendWith(MockitoExtension.class)
class AccountControllerBadRequestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService service;

    @MockBean
    private BeneficiaryService beneficiaryService;

    @MockBean
    private LoadDataService loadDataService;


    @Test
    void findByIdBadRequest() throws Exception {
        mockMvc.perform(get("/api/account/23df")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("The parameter type `23df` is invalid. `Long` type is required ")))
                .andExpect(jsonPath("$.status", is(400)));
    }

    @Test
    void createBeneficiaryBadRequest() throws Exception {
        String invalidRequestBody = "{"
                + "\"beneficiaryId\": \"1802\","
                + "\"balance\": \"invalid_input\","
                + "}";

        mockMvc.perform(post("/api/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Request parameters must be positive numbers")))
                .andExpect(jsonPath("$.status", is(400)));
    }

    @Test
    void updateBeneficiaryBadRequest() throws Exception {
        mockMvc.perform(delete("/api/account/23df")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("The parameter type `23df` is invalid. `Long` type is required ")))
                .andExpect(jsonPath("$.status", is(400)));
    }
}
