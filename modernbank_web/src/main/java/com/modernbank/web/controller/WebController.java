package com.modernbank.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {
    
    @RequestMapping(value="/main")
    public String mainPage() {
        return "catalog";
    }
    
    @RequestMapping(value="/customer/view/retrieveCustomer")
    public String retrieveCustomerPage() {
        return "customer/retrieveCustomer";
    }
    
    @RequestMapping(value="/customer/view/retrieveCustomerCQRS")
    public String retrieveCustomerCQRSPage() {
        return "customer/retrieveCustomerCQRS";
    }
    
    @RequestMapping(value="/customer/view/createCustomer")
    public String createCustomerPage() {
        return "customer/createCustomer";
    }
    
    @RequestMapping(value="/customer/view/retrieveDormantCustomer")
    public String retrieveDormantCustomerPage() {
        return "customer/retrieveDormantCustomer";
    }
    
    @RequestMapping(value="/transfer/view/transfer")
    public String transferPage() {
        return "transfer/transfer";
    }
    
    @RequestMapping(value="/transfer/view/btobTransfer")
    public String btobTransferPage() {
        return "transfer/btobTransfer";
    }
    
    @RequestMapping(value="/transfer/view/retrieveTransferHistory")
    public String transferHistoryPage() {
        return "transfer/transferHistory";
    }
    
    @RequestMapping(value="/account/view/createAccount")
    public String createAccountPage() {
        return "account/createAccount";
    }
    
    @RequestMapping(value="/account/view/deposit")
    public String depositPage() {
        return "account/deposit";
    }
    
    @RequestMapping(value="/account/view/withdraw")
    public String withdrawPage() {
        return "account/withdraw";
    }
    
    @RequestMapping(value="/account/view/retrieveTransactionHistory")
    public String retrieveTransactionHistoryPage() {
        return "account/retrieveTransactionHistory";
    }
}
