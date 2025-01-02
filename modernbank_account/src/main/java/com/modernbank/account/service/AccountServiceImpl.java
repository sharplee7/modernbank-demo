package com.modernbank.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.modernbank.account.domain.entity.Account;
import com.modernbank.account.domain.entity.TransactionHistory;
import com.modernbank.account.domain.entity.TransactionResult;
import com.modernbank.account.domain.repository.AccountRepository;
import com.modernbank.account.exception.BusinessException;
import com.modernbank.account.publisher.AccountProducer;
import com.modernbank.account.rest.customer.CustomerComposite;
import com.modernbank.account.rest.customer.entity.Customer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service("accountService")
public class AccountServiceImpl implements AccountService {

	@Autowired private AccountRepository accountRepository;
	@Autowired private AccountProducer accountProducer;
	@Autowired private CustomerComposite customerComposite;

    @Override
    public Account retrieveAccount(String acntNo) throws Exception {
    	Account account = accountRepository.selectAccount(Account.ofAcntNo(acntNo));

        if (account == null)
        	throw new BusinessException("존재하지않는 계좌번호입니다.");

        return account;
    }

    @Override //계좌번호 존재확인
    public boolean existsAccountNumber(String acntNo) throws Exception {
    	boolean ret = false;
    	
    	if (accountRepository.selectAccount(Account.ofAcntNo(acntNo)) != null)
    		ret = true;
    		
    	return ret;
    	
    }

    @Override //계좌 생성
    @Transactional(rollbackFor = Exception.class)
    public Integer createAccount(Account account) throws Exception {

        int result = 0;

        // 1) 계좌번호 중복 검증
        if(existsAccountNumber(account.getAcntNo()))
        	throw new BusinessException("존재하는 계좌번호입니다.");

        // 2) 고객정보 조회 (계좌테이블에 '고객이름' 저장을 위해)
        Customer customer = customerComposite.retrieveCustomer(account.getCstmId());
        account.setCstmNm(customer.getCstmNm());
        
        // 3) 계좌 생성
        result = accountRepository.insertAccount(account);
        
        // 4) 계좌 정보 message send
        accountProducer.sendCreatingAccountMessage(account);

        return result;
    }

    @Override //계좌목록 조회
    public List<Account> retrieveAccountList(String cstmId) throws Exception {
		return accountRepository.selectAccountList(Account.ofCstmId(cstmId));
    }

    @Override //계좌잔액 조회
	public Long retrieveAccountBalance(String acntNo) throws Exception {

		TransactionHistory transactionHistory = TransactionHistory.builder()
				.acntNo(acntNo).build();

		Long balance = accountRepository.selectCurrentAccountBalance(transactionHistory);

		if (balance == null)
			return 0L;
		else
			return balance;
	}

    @Override //거래내역 생성
    @Transactional(rollbackFor = Exception.class)
    public int createTransactionHistory(TransactionHistory transactionHistory) throws Exception {
    	return accountRepository.insertTransactionHistoryData(transactionHistory);
    }

    @Override //거래내역 목록 조회
    public List<TransactionHistory> retrieveTransactionHistoryList(String acntNo) throws Exception {
    	return accountRepository.selectTransactionHistoryList(TransactionHistory.ofAcntNo(acntNo));
    }

    @Override //입금
    @Transactional(rollbackFor = Exception.class)
    public TransactionResult deposit(TransactionHistory transactionHistory) throws Exception {

    	String acntNo = transactionHistory.getAcntNo();
		Long trnsAmt = transactionHistory.getTrnsAmt();

		// 1) 계좌잔액 조회
		Long acntBlnc = retrieveAccountBalance(acntNo);

        // 2) 입금 거래내역 생성
        transactionHistory.setAcntBlnc(acntBlnc + trnsAmt);
		transactionHistory.setDivCd("D");
		transactionHistory.setStsCd("1");
		createTransactionHistory(transactionHistory);

		// 3) 입금 거래내역 message send
		accountProducer.sendTransactionMessage(transactionHistory);

		// 4) 수정된 잔액 계좌정보 message send
        Account account = Account.of(acntNo, transactionHistory.getAcntBlnc());
		accountProducer.sendUpdatingAccountBalanceMessage(account);

		TransactionResult transactionResult = TransactionResult.builder()
                .acntNo(acntNo)
                .seq(transactionHistory.getSeq())  // 거래내역 생성 시 할당 된 seq 저장
                .formerBlnc(acntBlnc)
                .trnsAmt(trnsAmt)
                .acntBlnc(transactionHistory.getAcntBlnc())
                .build();

		return transactionResult;
    }

    @Override //출금
    @Transactional(rollbackFor = Exception.class)
    public TransactionResult withdraw(TransactionHistory transactionHistory) throws Exception {

    	String acntNo = transactionHistory.getAcntNo();
        Long trnsAmt = transactionHistory.getTrnsAmt();

        // 1) 계좌잔액 조회
        Long acntBlnc = retrieveAccountBalance(acntNo);

        // 2) 출금가능 확인
        if (acntBlnc < trnsAmt)
            throw new BusinessException("계좌 잔액이 부족합니다.");

        // 3) 출금 거래내역 생성
        transactionHistory.setAcntBlnc(acntBlnc - trnsAmt);
        transactionHistory.setDivCd("W");
        transactionHistory.setStsCd("1");
        createTransactionHistory(transactionHistory);

        // 4) 출금 거래내역 message send
        accountProducer.sendTransactionMessage(transactionHistory);

        // 5) 수정된 잔액 계좌정보 message send
        Account account = Account.of(acntNo, transactionHistory.getAcntBlnc());
        accountProducer.sendUpdatingAccountBalanceMessage(account);

        TransactionResult transactionResult = TransactionResult.builder()
                .acntNo(acntNo)
                .seq(transactionHistory.getSeq())  // 거래내역 생성 시 할당 된 seq 저장
                .formerBlnc(acntBlnc)
                .trnsAmt(trnsAmt)
                .acntBlnc(transactionHistory.getAcntBlnc())
                .build();

        return transactionResult;
        
    }

    @Override //출금 취소
    @Transactional(rollbackFor = Exception.class)
    public int cancelWithdraw(TransactionHistory transactionHistory) throws Exception {
    	
    	// 1) 출금 취소 상태로 거래내역 수정
        transactionHistory.setStsCd("2");
        int result = accountRepository.updateTransactionHistory(transactionHistory);

        // 2) 출금 취소 후 계좌 잔액 조회
        Long acntBlnc = retrieveAccountBalance(transactionHistory.getAcntNo());

        // 3) 출금 취소 거래내역 message send
        accountProducer.sendTransactionMessage(transactionHistory);

        // 4) 수정된 잔액 계좌정보 message send
        Account account = Account.of(transactionHistory.getAcntNo(), acntBlnc);
        accountProducer.sendUpdatingAccountBalanceMessage(account);

        return result;

    }

    @Override
    public int retrieveMaxSeq(String acntNo) throws Exception {
        return accountRepository.selectMaxSeq(TransactionHistory.ofAcntNo(acntNo));
    }
}
