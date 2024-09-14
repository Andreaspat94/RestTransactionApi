package com.apaterakis.resttransactionapi.controller;

import com.apaterakis.resttransactionapi.exception.DuplicateBeneficiaryException;
import com.apaterakis.resttransactionapi.exception.NotFoundException;
import com.apaterakis.resttransactionapi.model.Account;
import com.apaterakis.resttransactionapi.model.Beneficiary;
import com.apaterakis.resttransactionapi.model.BeneficiaryRequest;
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
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    private BeneficiaryRequest requestBody;
    @BeforeEach
    void setUp() {
        beneficiary = new Beneficiary("firstName", "lastName");
        account = new Account(beneficiary, new BigDecimal("5000.00"));
        beneficiary.setAccounts(List.of(account));
        requestBody = new BeneficiaryRequest("NewName", "NewSurname", new BigDecimal(400));
    }

    @Test
    void findById() {
        when(service.findById(any())).thenReturn(Optional.of(beneficiary));
        final ResponseEntity<Response> response = controller.findById(1L);
        verify(service).findById(any());

        assertEquals(beneficiary, response.getBody().getData());
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

    @Test
    void createBeneficiary() {
        Beneficiary newBeneficiary = new Beneficiary(requestBody.getFirstName(), requestBody.getLastName());
        account = new Account(beneficiary, new BigDecimal("5000.00"));
        newBeneficiary.setAccounts(List.of(account));

        when(service.existsByFirstNameAndLastName(any(), any())).thenReturn(false);
        when(service.createBeneficiary(requestBody)).thenReturn(newBeneficiary);

        final ResponseEntity<Response> response = controller.createBeneficiary(requestBody);

        verify(service).existsByFirstNameAndLastName(any(), any());
        verify(service).createBeneficiary(requestBody);

        assertEquals(newBeneficiary, response.getBody().getData());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void createBeneficiaryDuplicateBeneficiary() {
        when(service.existsByFirstNameAndLastName(any(), any()))
                .thenReturn(true);
        final DuplicateBeneficiaryException exc = assertThrows(DuplicateBeneficiaryException.class,
                () -> controller.createBeneficiary(requestBody));
        verify(service).existsByFirstNameAndLastName(any(), any());

        String actualMessage = exc.getMessage();
        String expectedMessage = "A beneficiary with the same first name and last name already exists.";

        assertEquals(409, exc.getStatus());
        assertThrows(DuplicateBeneficiaryException.class, () -> controller.createBeneficiary(requestBody));
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

    @Test
    void createBeneficiaryBadRequest() throws Exception {
        String invalidRequestBody = "{"
                + "\"firstName\": \"John\","
                + "\"lastName\": \"Doe\","
                + "\"firstDeposit\": \"invalid_string\""
                + "}";

        mockMvc.perform(post("/api/beneficiary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",is("Invalid input. `firstDeposit` must be a positive number")))
                .andExpect(jsonPath("$.status", is(400)));
    }
}
