package com.modernbank.cqrs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.modernbank.cqrs.domain.entity.Account;
import com.modernbank.cqrs.domain.entity.Customer;
import com.modernbank.cqrs.domain.repository.CQRSRepository;
import com.modernbank.cqrs.exception.BusinessException;
import com.modernbank.cqrs.exception.SystemException;

@Service("cqrsService")
public class CQRSServiceImpl implements CQRSService{
 
    @Autowired
    CQRSRepository cqrsRepository;

    @Override
    public Customer retrieveCustomerDetail(String cstmId) throws Exception {
    	Customer customer = null;
    	
    	// 고객 정보 조회
		customer = cqrsRepository.selectCustomer(Customer.builder().cstmId(cstmId).build());
	
		if (customer == null) 
			throw new BusinessException("존재하지 않는 아이디입니다.");
		
		// 계좌 목록 조회
		List<Account> accountList = retrieveAccountList(cstmId);   
		customer.addAllAccounts(accountList);
    	
        return customer;
    }

    /**
     * 사용자 id에  계좌 목록을 조회합니다.
     * @param cstmId
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public List<Account> retrieveAccountList(String cstmId) throws Exception {
    	List<Account> accountList = null;
		
		accountList= cqrsRepository.selectAccountList(Account.builder().cstmId(cstmId).build());
		
		if (accountList == null || accountList.size() <=0)
			throw new BusinessException(cstmId + " 님의 계좌 목록 정보를 조회하지 못했습니다.");
    	
    	return accountList;
    }
    
    /**
     * 사용자 정보를 생성합니다.
     */
    @Override
	public int createCustomer(Customer customer) throws Exception {
		return cqrsRepository.insertCustomer(customer);
	}

    /**
     * 계좌 정보를 생성합니다.
     */
	@Override
	public int createAccount(Account account) throws Exception {
		return cqrsRepository.insertAccount(account);
	}

	/**
	 * 이체 정보를 생성합니다.
	 */
	@Override
	public int updateTransferLimit(Customer customer) throws Exception {
		return cqrsRepository.updateTransferLimit(customer);
	}

	/**
	 * 계좌 잔액을 업데이트 합니다.
	 */
	@Override
	public int updateAccountBalance(Account account) throws Exception {
		return cqrsRepository.updateAccountBalance(account);
	}
	
}
