package com.modernbank.customer.controller;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.modernbank.customer.domain.entity.Customer;
import com.modernbank.customer.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;

@RestController
public class CustomerController {

    @Resource(name = "customerService")
    private CustomerService customerService;
    
    @Operation(summary =  "고객등록", method = "POST", description = "고객등록")
    @RequestMapping(method = RequestMethod.POST, path = "/")
    public Integer createCustomer(@RequestBody Customer customer) throws Exception{
         return customerService.createCustomer(customer);
    }

    @Operation(summary = "고객기본조회", method = "GET", description = "고객기본조회")
    @RequestMapping(method = RequestMethod.GET, path = "/{cstmId}")
    public Customer retrieveCustomer(@PathVariable(name = "cstmId") String cstmId) throws Exception{
        return customerService.retrieveCustomer(cstmId);
    }

    @Operation(summary = "고객상세조회", method = "GET", description = "고객상세조회")
    @RequestMapping(method = RequestMethod.GET, path = "/detail/{cstmId}")
    public Customer retrieveCustomerDetail(@PathVariable(name = "cstmId") String cstmId) throws Exception{
        return customerService.retrieveCustomerDetail(cstmId);
    }

    @Operation(summary = "고객존재여부조회", method = "GET", description = "고객존재여부조회")
    @RequestMapping(method = RequestMethod.GET, path ="/exists/{cstmId}")
    public Boolean existsCustomerId(@PathVariable(name = "cstmId") String cstmId) throws Exception{
    	return customerService.existsCustomerId(cstmId);
    }
    
}
