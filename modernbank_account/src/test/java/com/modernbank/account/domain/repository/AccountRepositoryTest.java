package com.modernbank.account.domain.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.modernbank.account.domain.entity.TransactionHistory;
import com.modernbank.account.domain.repository.AccountRepository;

@MybatisTest(properties = {
        "spring.datasource.schema=classpath:sql/modernbank_ACCOUNT_DDL.sql",
        "spring.datasource.data=classpath:sql/modernbank_ACCOUNT_DML.sql",
})
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    @Test
    void insertTransactionHistory_WithTransactionHistoryData_ReturnTransactionHistoryWithSeq() throws Exception {
        TransactionHistory transactionHistory = TransactionHistory.builder()
                .acntNo("222201")
                .divCd("D")
                .stsCd("1")
                .trnsAmt(1L)
                .acntBlnc(1000L)
                .trnsBrnch("역삼본점")
                .trnsDtm("2020-01-29 18:15:45").build();

        accountRepository.insertTransactionHistoryData(transactionHistory);

        Assertions.assertThat(transactionHistory.getSeq()).isGreaterThan(0);
    }
}