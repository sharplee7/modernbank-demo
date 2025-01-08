package com.modernbank.account.controller;

import java.util.List;
import com.modernbank.account.domain.entity.Account;
import com.modernbank.account.domain.entity.TransactionHistory;
import com.modernbank.account.domain.entity.TransactionResult;
import com.modernbank.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;

@RestController
public class AccountController {

    @Autowired
    @Resource(name = "accountService")
    private AccountService accountService;
    
    @Operation(summary = "Account Inquiry", method = "GET", description = "Retrieve account information")
    @GetMapping("/{acntNo}")
    public Account retrieveAccount(@PathVariable(name = "acntNo") String acntNo) throws Exception {
        return accountService.retrieveAccount(acntNo);
    }

    @Operation(summary = "Account Registration", method = "POST", description = "Register a new account")
    @PostMapping("/")
    public Integer createAccount(@RequestBody Account account) throws Exception {
        return accountService.createAccount(account);
    }

    @Operation(summary = "Account List Inquiry", method = "GET", description = "Retrieve list of accounts for a customer")
    @GetMapping("/customer/{cstmId}/accounts")
    public List<Account> retrieveAccountList(@PathVariable(name = "cstmId") String cstmId) throws Exception {
        return accountService.retrieveAccountList(cstmId);
    }

    @Operation(summary = "Account Balance Inquiry", method = "GET", description = "Retrieve account balance")
    @GetMapping("/{acntNo}/balance")
    public Long retrieveAccountBalance(@PathVariable(name = "acntNo") String acntNo) throws Exception {
        return accountService.retrieveAccountBalance(acntNo);
    }

    @Operation(summary = "Deposit", method = "POST", description = "Make a deposit")
    @PostMapping("/deposits/")
    public TransactionResult deposit(@RequestBody TransactionHistory input) throws Exception {
        return accountService.deposit(input);
    }

    @Operation(summary = "Withdrawal", method = "POST", description = "Make a withdrawal")
    @PostMapping("/withdrawals/")
    public TransactionResult withdraw(@RequestBody TransactionHistory input) throws Exception {
        return accountService.withdraw(input);
    }

    @Operation(summary = "Cancel Withdrawal", method = "POST", description = "Cancel a withdrawal")
    @PostMapping("/withdrawals/cancel/")
    public Integer cancelWithdraw(@RequestBody TransactionHistory input) throws Exception {
        return accountService.cancelWithdraw(input);
    }
    
    @Operation(summary = "Transaction History Inquiry", method = "GET", description = "Retrieve transaction history")
    @GetMapping("/{acntNo}/transactions")
    public List<TransactionHistory> retrieveTransactionHistory(@PathVariable(name = "acntNo") String acntNo) throws Exception {
        return accountService.retrieveTransactionHistoryList(acntNo);
    }
}