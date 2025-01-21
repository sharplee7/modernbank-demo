package com.modernbank.cqrs.subscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.modernbank.cqrs.domain.entity.Account;
import com.modernbank.cqrs.exception.SystemException;
import com.modernbank.cqrs.service.CQRSService;

import jakarta.annotation.Resource;

@Component
public class AccountConsumer {
	@Resource(name = "cqrsService")
    private CQRSService cqrsService;
	private final Logger LOGGER = LoggerFactory.getLogger(AccountConsumer.class);

	
    @KafkaListener(topics = "${creating.account.topic.name}", containerFactory = "accountKafkaListenerContainerFactory")
    public void creatingAccountListener(Account account, Acknowledgment ack) {
    	LOGGER.info("Recieved creating account message: " + account.getAcntNo());
  
        try {
        	/*고객상세조회 : 계좌 데이터 등록*/
        	cqrsService.createAccount(account);
 	
           	ack.acknowledge();  // 성공시 커밋
           	
        } catch(Exception e) {
        	String msg = " 계좌 데이터 또는 계좌 이력 데이터 등록에 문제가 발생했습니다.";
        	LOGGER.error(account.getAcntNo() + msg,e);
        } 
    }
    
    /**
     * 계좌 잔액 업데이트
     * @param account
     * @throws SystemException
     */
    @KafkaListener(topics = "${updating.account.balance.topic.name}", containerFactory = "accountKafkaListenerContainerFactory")
    public void updatingAccountBalanceListener(Account account, Acknowledgment ack) {
    	LOGGER.info("Recieved updating account balance account message: " + account.getAcntNo());
    	
        try {
        	cqrsService.updateAccountBalance(account);
          	ack.acknowledge();
        }catch(Exception e) {
        	String msg = " 계좌 잔액 업데이트에 문제가 발생했습니다.";
        	LOGGER.error(account.getAcntNo() + msg,e);
        } 
    }
    
}
