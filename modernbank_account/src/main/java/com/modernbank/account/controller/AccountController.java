package com.modernbank.account.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.modernbank.account.domain.entity.Account;
import com.modernbank.account.domain.entity.TransactionHistory;
import com.modernbank.account.domain.entity.TransactionResult;
import com.modernbank.account.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;

@RestController
public class AccountController {

	@Autowired
    @Resource(name = "accountService")
    private AccountService accountService;
    
    @Operation(summary = "계좌조회", method = "GET", description = "계좌조회")
    @GetMapping("/{acntNo}")
    public Account retrieveAccount(@PathVariable(name = "acntNo") String acntNo) throws Exception{
        return accountService.retrieveAccount(acntNo);
    }

    @Operation(summary = "계좌등록", method = "POST", description = "계좌등록")
    @PostMapping("/")
    public Integer createAccount(@RequestBody Account account) throws Exception{
    	return accountService.createAccount(account);
    }

    @Operation(summary = "계좌목록조회", method = "GET", description = "계좌목록조회")
    @GetMapping("/list/{cstmId}")
    public List<Account> retrieveAccountList(@PathVariable(name = "cstmId") String cstmId) throws Exception{
        List<Account> accountList =  accountService.retrieveAccountList(cstmId);
        return accountList;
    }

    @Operation(summary = "계좌잔액조회", method = "GET", description = "계좌잔액조회")
    @GetMapping("/balance/{acntNo}")
    public Long retrieveAccountBalance(@PathVariable(name = "acntNo") String acntNo) throws Exception{
        return accountService.retrieveAccountBalance(acntNo);
    }

    @Operation(summary = "입금", method = "POST", description = "입금")
    @PostMapping("/deposit/")
    public TransactionResult deposit(@RequestBody TransactionHistory input) throws Exception{
        return accountService.deposit(input);
    }

    @Operation(summary = "출금", method = "POST", description = "출금")
    @PostMapping("/withdraw/")
    public TransactionResult withdraw(@RequestBody TransactionHistory input) throws Exception{
        return  accountService.withdraw(input);
    }

    @Operation(summary = "출금취소", method = "POST", description = "출금취소")
    @PostMapping("/withdraw/cancel/")
    public Integer cancelWithdraw(@RequestBody TransactionHistory input) throws Exception{
        return  accountService.cancelWithdraw(input);
    }
    
    @Operation(summary = "거래내역조회", method = "GET", description = "거래내역조회")
    @GetMapping("/transaction-history/{acntNo}")
    public List<TransactionHistory> retrieveTransactionHistory(@PathVariable(name = "acntNo") String acntNo) throws Exception{
        List<TransactionHistory> transactionHistory = accountService.retrieveTransactionHistoryList(acntNo);
        return transactionHistory;
    }
}
