package com.modernbank.account.rest.customer;

import com.modernbank.account.rest.customer.entity.Customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service("customerComposite")
public class CustomerCompositeImpl implements CustomerComposite {

    private static Logger LOGGER = LoggerFactory.getLogger(CustomerCompositeImpl.class);
	
    @Value("${customer.api.url}")
    private String CUSTOMER_API_URL;
    
    @Autowired
    private final RestTemplate restTemplate;
    
    public CustomerCompositeImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Customer retrieveCustomer(String cstmId) throws Exception {
        String apiUrl =  "/{cstmId}";
        return this.restTemplate.getForObject(CUSTOMER_API_URL + apiUrl, Customer.class, cstmId);
    }

    public Customer fallbackRetriveCustomer(String cstmId, Throwable t) throws Exception {
        String msg = "There is a problem calling the customer information retrieval service for " + cstmId + " using restTemplate.";
        LOGGER.error(msg, t);
        throw new Exception();
    }
}