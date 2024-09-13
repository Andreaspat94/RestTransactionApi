package com.apaterakis.resttransactionapi.controller;

import com.apaterakis.resttransactionapi.exception.NotFoundException;
import com.apaterakis.resttransactionapi.model.Response;
import com.apaterakis.resttransactionapi.model.Transaction;
import com.apaterakis.resttransactionapi.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transactions of a beneficiary by id", description = "Returns the transactions list based on id. " +
            "If not found, then throws NotFoundException.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions found."),
            @ApiResponse(responseCode = "400", description = "BadRequest. The parameter type is invalid.",
                    content = @Content(examples = @ExampleObject(value = """
                       {
                           "status": 400,
                           "message": "The parameter type `23fff` is invalid. `Long` type is required",
                           "successful": false
                       }
                    """))),
            @ApiResponse(responseCode = "404", description = "Transactions not found.",
                    content = @Content(examples = @ExampleObject(value = """
                       {
                         "status": 404,
                         "message": "No transactions found.",
                         "successful": false
                       }
                    """)))
    })
    public ResponseEntity<Response> findByBeneficiaryId(@PathVariable("id") Long id) {
        List<Transaction> transactionList = transactionService.findByBeneficiaryId(id);

        if (transactionList.isEmpty())
            throw new NotFoundException(404, "No transactions found.");

        return ResponseEntity.ok(new Response(HttpStatus.OK.value(),
                "Transactions found.",
                        transactionList,
                        true
        ));
    }

}
