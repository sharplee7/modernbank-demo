package com.modernbank.cqrs.controller;

import com.modernbank.cqrs.domain.entity.Customer;
import com.modernbank.cqrs.service.CQRSService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@RestController
public class CQRSController {

    private final CQRSService cqrsService;

    @Autowired
    public CQRSController(CQRSService cqrsService) {
        this.cqrsService = cqrsService;
    }
    
    @Operation(summary = "Retrieve customer details", method = "GET", description = "Get detailed information about a specific customer")
    @GetMapping("/customers/{cstmId}/details")
    public Customer retrieveCustomerDetail(@PathVariable("cstmId") String cstmId) throws Exception {
        return cqrsService.retrieveCustomerDetail(cstmId);
    }
}