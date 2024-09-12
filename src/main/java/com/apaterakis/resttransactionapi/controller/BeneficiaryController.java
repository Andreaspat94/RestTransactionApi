package com.apaterakis.resttransactionapi.controller;

import com.apaterakis.resttransactionapi.exception.NotFoundException;
import com.apaterakis.resttransactionapi.model.Beneficiary;
import com.apaterakis.resttransactionapi.model.Response;
import com.apaterakis.resttransactionapi.service.BeneficiaryService;
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
