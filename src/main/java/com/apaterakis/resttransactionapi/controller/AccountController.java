package com.apaterakis.resttransactionapi.controller;

import com.apaterakis.resttransactionapi.exception.NotFoundException;
import com.apaterakis.resttransactionapi.model.Account;
import com.apaterakis.resttransactionapi.model.AccountRequest;
import com.apaterakis.resttransactionapi.model.Response;
import com.apaterakis.resttransactionapi.service.AccountService;
import com.apaterakis.resttransactionapi.service.BeneficiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;
    private final BeneficiaryService beneficiaryService;

    @Autowired
    public AccountController(AccountService accountService, BeneficiaryService beneficiaryService) {
        this.accountService = accountService;
        this.beneficiaryService = beneficiaryService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Account by id", description = "Returns the account based on id. " +
            "If not found, then throws NotFoundException.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account found."),
            @ApiResponse(responseCode = "400", description = "BadRequest. The parameter type is invalid.",
                    content = @Content(examples = @ExampleObject(value = """
                       {
                           "status": 400,
                           "message": "The parameter type `23fff` is invalid. `Long` type is required",
                           "successful": false
                       }
                    """))),
            @ApiResponse(responseCode = "404", description = "Account has not found.",
                    content = @Content(examples = @ExampleObject(value = """
                       {
                         "status": 404,
                         "message": "Account has not found.",
                         "successful": false
                       }
                    """)))
    })
    public ResponseEntity<Response> findById(@PathVariable("id") Long id) {
        Optional<Account> account = accountService.findById(id);

        if (account.isEmpty())
            throw new NotFoundException("Account has not found.");

        return ResponseEntity.ok(new Response(HttpStatus.OK.value(),
                "Account found.",
                account.get(),
                true
        ));
    }


    @PostMapping()
    @Operation(summary = "Creates new account.", description = "Creates a new account that associates with a beneficiary " )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created."),
            @ApiResponse(responseCode = "400", description = "BadRequest. The parameter type is invalid.",
                    content = @Content(examples = @ExampleObject(value = """
                       {
                           "status": 400,
                           "message": "Invalid input. `firstDeposit` must be a positive number",
                           "successful": false
                       }
                    """)))
    })
    public ResponseEntity<Response> createAccount(@RequestBody AccountRequest request) {

        Account newAccount = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(new Response(
                HttpStatus.CREATED.value(),
                "Account created.",
                newAccount,
                true
        ));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes Account.", description = "Deletes an account by its id" +
            "If account not found, then throws NotFoundException.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account deleted."),
            @ApiResponse(responseCode = "400", description = "BadRequest. The parameter type is invalid.",
                    content = @Content(examples = @ExampleObject(value = """
                       {
                           "status": 400,
                           "message": "The parameter type `1004ff` is invalid. `Long` type is required ",
                           "successful": false
                       }
                    """))),
            @ApiResponse(responseCode = "404", description = " Account has not found.",
                    content = @Content(examples = @ExampleObject(value = """
                       {
                         "status": 404,
                         "message": "Beneficiary has not found.",
                         "successful": false
                       }
                    """)))
    })
    public ResponseEntity<Response> deleteAccount(@PathVariable("id") Long id) {
        Optional<Account> accountOptional = accountService.findById(id);

        if (accountOptional.isEmpty())
            throw new NotFoundException("Account has not found.");

        accountService.deleteAccount(id);

        return ResponseEntity.ok(new Response(HttpStatus.OK.value(),
                "Account with id: " + id + " is deleted.",
                null,
                true
        ));

    }

}
