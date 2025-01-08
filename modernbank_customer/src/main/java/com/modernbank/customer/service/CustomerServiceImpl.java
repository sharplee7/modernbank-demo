package com.modernbank.customer.service;

import java.util.List;
import com.modernbank.customer.domain.entity.Customer;
import com.modernbank.customer.domain.repository.CustomerRepository;
import com.modernbank.customer.exception.BusinessException;
import com.modernbank.customer.exception.SystemException;
import com.modernbank.customer.publisher.CustomerProducer;
import com.modernbank.customer.rest.account.entity.Account;
import com.modernbank.customer.rest.transfer.entity.TransferLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerProducer customerProducer;

    @Autowired
    RestTemplate restTemplate;

    @Value("${transfer.api.url}")
    private String transferServiceUrl;

    @Value("${account.api.url}")
    private String accountServiceUrl;

    /**
     * @Transactional 어노테이션은 RuntimeException을 상속받은 Exception이 throws 될 때 작동
     * Exception 혹은 사용자 에러는 @Transactional(rollbackFor = {Exception.class}) 처럼 명시
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createCustomer(Customer customer) throws Exception {
        String cstmId = customer.getCstmId();
        int result = 0;

        if (existsCustomerId(cstmId))
            throw new BusinessException("이미 존재하는 아이디입니다.");
        
        // 고객 정보 저장
        customerRepository.insertCustomer(customer);
      
        // RestTemplate을 이용한 통신시 에러 발생하면 RuntimeException을 상속받은 SystemException throw
        // 따라서, Transaction Rollback 발생
        try {
            restTemplate.postForObject(
                transferServiceUrl + "/limits/",
                TransferLimit.builder()
                    .cstmId(cstmId)
                    .oneDyTrnfLmt(500000000L)
                    .oneTmTrnfLmt(500000000L)
                    .build(),
                Integer.class
            );
        } catch (Exception e) {
            throw new SystemException("이체 한도 설정 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        // 카프카 전송은 모든 트랜잭션 처리가 완료된 후에 수행, 카프카 역시 SystemException throw
        // 생각해볼 문제, insertCustomer OK, createTransferLimit OK, 
        // but sendCreatingCustomerMessage NOK인 경우엔 어떻게 하지?
        customerProducer.sendCreatingCustomerMessage(customer);
            
        return result;
    }

    @Override
    public Customer retrieveCustomer(String cstmId) throws Exception {
        Customer customer = new Customer();
        customer.setCstmId(cstmId);
  
        if (!existsCustomerId(cstmId)) 
            throw new BusinessException("존재하지 않는 아이디입니다.");

        customer = customerRepository.selectCustomer(customer);

        if (customer == null) 
            throw new BusinessException("고객 데이터를 조회하지 못했습니다.");
        

        return customer;
    }

    @Override
    public Customer retrieveCustomerDetail(String cstmId) throws Exception {
        Customer customer = new Customer();
        customer.setCstmId(cstmId);
        
        if (!existsCustomerId(cstmId)) 
            throw new BusinessException("존재하지 않는 아이디입니다.");

        // 고개 데이터 조회
        customer = customerRepository.selectCustomer(customer);
        if (customer == null) 
            throw new BusinessException("고객 데이터를 조회하지 못했습니다.");

        // 이체 한도 조회
        try {
            TransferLimit transferLimit = restTemplate.getForObject(
                transferServiceUrl + "/limits/{cstmId}",
                TransferLimit.class,
                cstmId
            );
            if (transferLimit == null) 
                throw new BusinessException("이체 한도 정보를 조회하지 못했습니다.");

            customer.setOneDyTrnfLmt(transferLimit.getOneDyTrnfLmt());
            customer.setOneTmTrnfLmt(transferLimit.getOneTmTrnfLmt());
        } catch (Exception e) {
            throw new SystemException("이체 한도 조회 중 오류가 발생했습니다: " + e.getMessage());
        }

        // 계좌 목록 조회
        try {
            ResponseEntity<List<Account>> response = restTemplate.exchange(
                accountServiceUrl + "/customer/{cstmId}/accounts",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Account>>() {},
                cstmId
            );
            List<Account> accountList = response.getBody();
            if (accountList == null) 
                throw new BusinessException(cstmId + "님의 계좌 목록 정보를 조회하지 못했습니다.");

            customer.addAllAccounts(accountList);
        } catch (Exception e) {
            throw new SystemException("계좌 목록 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
            
        return customer;
    }

    @Override
    public boolean existsCustomerId(String cstmId) throws Exception {
        boolean ret = false;
        
        // 이미 존재하는 아이디라면 true 리턴
        Customer customer = new Customer();
        customer.setCstmId(cstmId);
        if(customerRepository.existsCustomer(customer) > 0)
            ret = true;
          
        return ret;
    }

}