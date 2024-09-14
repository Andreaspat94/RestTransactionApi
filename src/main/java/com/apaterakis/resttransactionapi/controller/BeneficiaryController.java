package com.apaterakis.resttransactionapi.controller;

import com.apaterakis.resttransactionapi.exception.DuplicateBeneficiaryException;
import com.apaterakis.resttransactionapi.exception.NotFoundException;
import com.apaterakis.resttransactionapi.model.Beneficiary;
import com.apaterakis.resttransactionapi.model.BeneficiaryRequest;
import com.apaterakis.resttransactionapi.model.Response;
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
@RequestMapping("/api/beneficiary")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    @Autowired
    public BeneficiaryController(BeneficiaryService beneficiaryService) {
        this.beneficiaryService = beneficiaryService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get beneficiary by id", description = "Returns the beneficiary based on id. " +
            "If not found, then throws NotFoundException.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Beneficiary found."),
            @ApiResponse(responseCode = "400", description = "BadRequest. The parameter type is invalid.",
                    content = @Content(examples = @ExampleObject(value = """
                       {
                           "status": 400,
                           "message": "The parameter type `23fff` is invalid. `Long` type is required",
                           "successful": false
                       }
                    """))),
            @ApiResponse(responseCode = "404", description = "Beneficiary has not found.",
                    content = @Content(examples = @ExampleObject(value = """
                       {
                         "status": 404,
                         "message": "Beneficiary has not found.",
                         "successful": false
                       }
                    """)))
    })
    public ResponseEntity<Response> findById(@PathVariable("id") Long id) {
        Optional<Beneficiary> beneficiary = beneficiaryService.findById(id);

        if (beneficiary.isEmpty())
            throw new NotFoundException("Beneficiary has not found.");

        return ResponseEntity.ok(new Response(HttpStatus.OK.value(),
                "Beneficiary found.",
                beneficiary.get(),
                true
        ));
    }

    @PostMapping()
    @GetMapping("/{id}")
    @Operation(summary = "Creates new beneficiary.", description = "Creates a new beneficiary with a new account. " +
            "If beneficiary with same first and last name already exists, then throws DuplicateBeneficiaryException.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Beneficiary created."),
            @ApiResponse(responseCode = "400", description = "BadRequest. The parameter type is invalid.",
                    content = @Content(examples = @ExampleObject(value = """
                       {
                           "status": 400,
                           "message": "Invalid input. `firstDeposit` must be a positive number",
                           "successful": false
                       }
                    """))),
            @ApiResponse(responseCode = "409", description = "A beneficiary with the same first name and last name already exists.",
                    content = @Content(examples = @ExampleObject(value = """
                       {
                         "status": 404,
                         "message": "A beneficiary with the same first name and last name already exists.",
                         "successful": false
                       }
                    """)))
    })
    public ResponseEntity<Response> createBeneficiary(@RequestBody BeneficiaryRequest request) {
        boolean beneficiaryExist = beneficiaryService.existsByFirstNameAndLastName(request.getFirstName(), request.getLastName());
        if (beneficiaryExist) {
            throw new DuplicateBeneficiaryException("A beneficiary with the same first name and last name already exists.");
        }

        Beneficiary newBeneficiary = beneficiaryService.createBeneficiary(request);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(new Response(
                HttpStatus.CREATED.value(),
                "Beneficiary created.",
                newBeneficiary,
                true
        ));
    }
}
