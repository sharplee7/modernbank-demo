package com.modernbank.customer.service;

import java.util.List;

import com.modernbank.customer.domain.entity.Customer;
import com.modernbank.customer.domain.repository.CustomerRepository;
import com.modernbank.customer.exception.BusinessException;
import com.modernbank.customer.exception.SystemException;
import com.modernbank.customer.publisher.CustomerProducer;
import com.modernbank.customer.rest.account.entity.Account;
import com.modernbank.customer.rest.transfer.entity.TransferLimit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerProducer customerProducer;

    @Autowired
    RestTemplate restTemplate;

    @Value("${transfer.api.url}")
    private String transferServiceUrl;

    @Value("${account.api.url}")
    private String accountServiceUrl;

    /**
     * The @Transactional annotation works when an Exception that inherits RuntimeException is thrown
     * For Exception or user errors, specify like @Transactional(rollbackFor = {Exception.class})
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createCustomer(Customer customer) throws Exception {
        String cstmId = customer.getCstmId();
        int result = 0;

        if (existsCustomerId(cstmId))
            throw new BusinessException("ID already exists.");
        
        // Save customer information
        customerRepository.insertCustomer(customer);
      
        // When an error occurs during communication using RestTemplate, it throws SystemException which inherits RuntimeException
        // Therefore, Transaction Rollback occurs
        try {
            restTemplate.postForObject(
                transferServiceUrl + "/limits/",
                TransferLimit.builder()
                    .cstmId(cstmId)
                    .oneDyTrnfLmt(500000000L)
                    .oneTmTrnfLmt(500000000L)
                    .build(),
                Integer.class
            );
        } catch (Exception e) {
            throw new SystemException("An error occurred while setting transfer limits: " + e.getMessage());
        }
        
        // Kafka transmission is performed after all transaction processing is completed, Kafka also throws SystemException
        // Issue to consider: what if insertCustomer OK, createTransferLimit OK, 
        // but sendCreatingCustomerMessage NOK?
        customerProducer.sendCreatingCustomerMessage(customer);
            
        return result;
    }

    @Override
    public Customer retrieveCustomer(String cstmId) throws Exception {
        Customer customer = new Customer();
        customer.setCstmId(cstmId);
  
        if (!existsCustomerId(cstmId)) 
            throw new BusinessException("ID does not exist.");

        customer = customerRepository.selectCustomer(customer);

        if (customer == null) 
            throw new BusinessException("Failed to retrieve customer data.");
        

        return customer;
    }

    @Override
    public Customer retrieveCustomerDetail(String cstmId) throws Exception {
        Customer customer = new Customer();
        customer.setCstmId(cstmId);
        
        if (!existsCustomerId(cstmId)) 
            throw new BusinessException("ID does not exist.");

        // Retrieve customer data
        customer = customerRepository.selectCustomer(customer);
        if (customer == null) 
            throw new BusinessException("Failed to retrieve customer data.");

        // Retrieve transfer limits
        try {
            TransferLimit transferLimit = restTemplate.getForObject(
                transferServiceUrl + "/limits/{cstmId}",
                TransferLimit.class,
                cstmId
            );
            if (transferLimit == null) 
                throw new BusinessException("Failed to retrieve transfer limit information.");

            customer.setOneDyTrnfLmt(transferLimit.getOneDyTrnfLmt());
            customer.setOneTmTrnfLmt(transferLimit.getOneTmTrnfLmt());
        } catch (Exception e) {
            throw new SystemException("An error occurred while retrieving transfer limits: " + e.getMessage());
        }

        // Retrieve account list
        try {
            ResponseEntity<List<Account>> response = restTemplate.exchange(
                accountServiceUrl + "/customer/{cstmId}/accounts",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Account>>() {},
                cstmId
            );
            List<Account> accountList = response.getBody();
            if (accountList == null) 
                throw new BusinessException("Failed to retrieve account list for customer " + cstmId);

            customer.addAllAccounts(accountList);
        } catch (Exception e) {
            throw new SystemException("An error occurred while retrieving account list: " + e.getMessage());
        }
            
        return customer;
    }

    @Override
    public boolean existsCustomerId(String cstmId) throws Exception {
        boolean ret = false;
        
        // If the ID already exists, return true
        Customer customer = new Customer();
        customer.setCstmId(cstmId);
        if(customerRepository.existsCustomer(customer) > 0)
            ret = true;
          
        return ret;
    }

}