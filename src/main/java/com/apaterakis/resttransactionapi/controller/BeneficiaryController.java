package com.apaterakis.resttransactionapi.controller;

import com.apaterakis.resttransactionapi.exception.NotFoundException;
import com.apaterakis.resttransactionapi.model.Beneficiary;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @Operation(summary = "Get location by latitude, longitude", description = "Returns the location based on the provided coordinates. If not found, then finds and returns the nearest point of interest.")
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
            throw new NotFoundException(404, "Beneficiary has not found.");

        return ResponseEntity.ok(new Response(HttpStatus.OK.value(),
                "Beneficiary found.",
                beneficiary.get(),
                true
        ));

    }
}
