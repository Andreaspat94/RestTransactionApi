package com.apaterakis.resttransactionapi;

import com.apaterakis.resttransactionapi.controller.BeneficiaryController;
import com.apaterakis.resttransactionapi.exception.NotFoundException;
import com.apaterakis.resttransactionapi.model.Account;
import com.apaterakis.resttransactionapi.model.Beneficiary;
import com.apaterakis.resttransactionapi.model.Response;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BeneficiaryControllerTest {
    @Mock
    private BeneficiaryService service;

    @InjectMocks
    private BeneficiaryController controller;

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
        when(service.findById(any())).thenReturn(Optional.of(beneficiary));
        final ResponseEntity<Response> response = controller.findById(1L);
        verify(service).findById(any());

        assertEquals(beneficiary, response.getBody().getBeneficiary());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void findByIdNotFound() {
        when(service.findById(any())).thenReturn(Optional.empty());
        final NotFoundException exc = assertThrows(NotFoundException.class,
                () -> controller.findById(1L));
        verify(service).findById(any());

        String actualMessage = exc.getMessage();
        String expectedMessage = "Beneficiary has not found.";

        assertEquals(404, exc.getStatus());
        assertThrows(NotFoundException.class, () -> controller.findById(1L));
        assertTrue(actualMessage.contains(expectedMessage));
    }
}

@WebMvcTest(BeneficiaryController.class)
@ExtendWith(MockitoExtension.class)
class BeneficiaryControllerBadRequestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BeneficiaryService service;

    @MockBean
    private LoadDataService loadDataService;


    @Test
    void findByIdBadRequest() throws Exception {
        mockMvc.perform(get("/api/beneficiary/23df")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",is("The parameter type `23df` is invalid. `Long` type is required ")))
                .andExpect(jsonPath("$.status", is(400)));
    }
}
