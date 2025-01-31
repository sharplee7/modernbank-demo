package com.modernbank.account.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.modernbank.account.domain.entity.Account;
import com.modernbank.account.domain.repository.AccountRepository;
import com.modernbank.account.exception.BusinessException;
import com.modernbank.account.publisher.AccountProducer;
import com.modernbank.account.rest.customer.CustomerComposite;
import com.modernbank.account.rest.customer.entity.Customer;
import com.modernbank.account.service.AccountServiceImpl;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock private AccountRepository accountRepository;
    @Mock private AccountProducer accountProducer;
    @Mock private CustomerComposite customerComposite;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void retrieveAccount_WithActNo_ReturnAccount() throws Exception {
        //given
        String acntNo = "111101";

        Account account = Account.builder()
                .acntNo(acntNo)
                .cstmId("1111")
                .cstmNm("홍길동")
                .acntNm("샘플계좌")
                .newDtm("2020-01-29 18:10:44").build();

        given(accountRepository.selectAccount(any(Account.class))).willReturn(account);

        //when
        accountService.retrieveAccount(acntNo);

        //then
        verify(accountRepository).selectAccount(Account.ofAcntNo(acntNo));
    }

    @Test
    void retrieveAccount_WithNotExistActNo_ThrowBusinessException() throws Exception {
        //given
        String acntNo = "111102";

        given(accountRepository.selectAccount(any(Account.class))).willReturn(null);

        //when
        Throwable exception = assertThrows(BusinessException.class, () -> accountService.retrieveAccount(acntNo));

        //then
        assertThat(exception.getMessage()).isEqualTo("존재하지않는 계좌번호입니다.");
    }

    @Test
    void createAccount_WithNotExistAccount_WillCreateAccount() throws Exception {
        //given
        Account account = Account.builder()
                .acntNo("111101")
                .cstmId("1111")
                .cstmNm("홍길동")
                .acntNm("샘플계좌")
                .newDtm("2020-01-29 18:10:44").build();

        Customer customer = Customer.builder()
                .cstmId(account.getCstmId())
                .cstmNm(account.getCstmNm()).build();

        given(accountRepository.selectAccount(any(Account.class))).willReturn(null);
        given(customerComposite.retrieveCustomer(anyString())).willReturn(customer);
        given(accountRepository.insertAccount(account)).willReturn(1);

        //when
        accountService.createAccount(account);

        //then
        verify(customerComposite).retrieveCustomer(account.getCstmId());
        verify(accountRepository).insertAccount(account);
        verify(accountProducer).sendCreatingAccountMessage(account);
    }

    @Test
    void createAccount_WithExistAccount_ThrowBusinessException() throws Exception {

        //given
        Account account = Account.builder()
                .acntNo("111101")
                .cstmId("1111")
                .cstmNm("홍길동")
                .acntNm("샘플계좌")
                .newDtm("2020-01-29 18:10:44").build();

        given(accountRepository.selectAccount(any(Account.class))).willReturn(account);

        //when
        Throwable exception = assertThrows(BusinessException.class, () -> accountService.createAccount(account));

        //then
        assertThat(exception.getMessage()).isEqualTo("존재하는 계좌번호입니다.");
    }

}