package com.apaterakis.resttransactionapi.controller;

import com.apaterakis.resttransactionapi.exception.NotFoundException;
import com.apaterakis.resttransactionapi.model.Response;
import com.apaterakis.resttransactionapi.model.Transaction;
import com.apaterakis.resttransactionapi.model.TransactionType;
import com.apaterakis.resttransactionapi.service.BeneficiaryService;
import com.apaterakis.resttransactionapi.service.LoadDataService;
import com.apaterakis.resttransactionapi.service.TransactionService;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    @Mock
    private TransactionService service;

    @InjectMocks
    private TransactionController controller;

    private Transaction transaction1;
    private Transaction transaction2;
    private List<Transaction> transactions;

    @BeforeEach
    void setUp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        LocalDate date = LocalDate.parse("01/10/23", formatter);
        transaction1 = new Transaction(1L,new BigDecimal(200), TransactionType.WITHDRAWAL, date);
        transaction2 = new Transaction(2L,new BigDecimal(100), TransactionType.DEPOSIT, date);

        transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
    }

    @Test
    void findByBeneficiaryId() {
        when(service.findByBeneficiaryId(any())).thenReturn(transactions);
        final ResponseEntity<Response> response = controller.findByBeneficiaryId(1L);
        verify(service).findByBeneficiaryId(any());

        assertEquals(transactions, response.getBody().getData());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void findByBeneficiaryIdNotFound() {
        when(service.findByBeneficiaryId(any())).thenReturn(List.of());
        final NotFoundException exc = assertThrows(NotFoundException.class,
                () -> controller.findByBeneficiaryId(1L));
        verify(service).findByBeneficiaryId(any());

        String actualMessage = exc.getMessage();
        String expectedMessage = "No transactions found.";

        assertEquals(404, exc.getStatus());
        assertThrows(NotFoundException.class, () -> controller.findByBeneficiaryId(1L));
        assertTrue(actualMessage.contains(expectedMessage));
    }
}

@WebMvcTest(TransactionController.class)
@ExtendWith(MockitoExtension.class)
class TransactionControllerBadRequestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService service;

    @MockBean
    private BeneficiaryService beneficiaryService;

    @MockBean
    private LoadDataService loadDataService;


    @Test
    void findByIdBadRequest() throws Exception {
        mockMvc.perform(get("/api/transaction/23df")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("The parameter type `23df` is invalid. `Long` type is required ")))
                .andExpect(jsonPath("$.status", is(400)));
    }
}