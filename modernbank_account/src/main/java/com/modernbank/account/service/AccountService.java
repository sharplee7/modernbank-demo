package com.modernbank.account.service;

import java.util.List;

import com.modernbank.account.domain.entity.Account;
import com.modernbank.account.domain.entity.TransactionHistory;
import com.modernbank.account.domain.entity.TransactionResult;

public interface AccountService {
    Account retrieveAccount(String acntNo) throws Exception;
    Integer createAccount(Account account) throws Exception;
    boolean existsAccountNumber(String acntNo) throws Exception;
    List<Account> retrieveAccountList(String cstmId) throws Exception;
    Long retrieveAccountBalance(String acntNo) throws Exception;
    TransactionResult deposit(TransactionHistory transactionHistory) throws Exception;
    TransactionResult withdraw(TransactionHistory transactionHistory) throws Exception;
    int createTransactionHistory(TransactionHistory transactionHistory) throws Exception;
    List<TransactionHistory> retrieveTransactionHistoryList(String acntNo) throws Exception;
    int cancelWithdraw(TransactionHistory transactionHistory) throws Exception;
    int retrieveMaxSeq(String acntNo) throws Exception;
}
