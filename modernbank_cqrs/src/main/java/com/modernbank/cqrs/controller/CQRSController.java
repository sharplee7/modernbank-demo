package com.modernbank.cqrs.controller;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.modernbank.cqrs.domain.entity.Customer;
import com.modernbank.cqrs.service.CQRSService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;

@RestController
public class CQRSController {

    @Resource(name = "cqrsService")
    private CQRSService cqrsService;
    
    @Operation(summary = "고객상세조회", method = "GET", description = "고객상세조회")
    @RequestMapping(method = RequestMethod.GET, path = "/detail/{cstmId}")
    public Customer retrieveCustomerDetail(@PathVariable(name = "cstmId") String cstmId) throws Exception{
        return cqrsService.retrieveCustomerDetail(cstmId);
    }

}
