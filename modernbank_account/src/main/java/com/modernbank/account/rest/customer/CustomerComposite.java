package com.modernbank.account.rest.customer;

import com.modernbank.account.rest.customer.entity.Customer;

public interface CustomerComposite {
    Customer retrieveCustomer(String cstmId) throws Exception;
}
