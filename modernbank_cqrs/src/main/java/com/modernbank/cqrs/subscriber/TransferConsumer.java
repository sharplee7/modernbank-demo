package com.modernbank.cqrs.subscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.modernbank.cqrs.domain.entity.Customer;
import com.modernbank.cqrs.exception.SystemException;
import com.modernbank.cqrs.service.CQRSService;

import jakarta.annotation.Resource;

@Component
public class TransferConsumer {
	
	@Resource(name = "cqrsService")
    private CQRSService cqrsService;
	private final Logger LOGGER = LoggerFactory.getLogger(TransferConsumer.class);
	
    /**
     * 이체 정보를  업데이트 합니다.
     * @param customer
     * @throws SystemException
     */
    @KafkaListener(topics = "${updating.transfer.limit.topic.name}", containerFactory = "transferLimitKafkaListenerContainerFactory")
    public void updatingTransferLimitListener(Customer customer, Acknowledgment ack) {
    	LOGGER.info("Recieved updating transfer limit message: " + customer.getCstmId());
        try {
            System.out.println("============> 이체한도를 리스너를 타고 있습니다.");     	
        	cqrsService.updateTransferLimit(customer);
        	
           	ack.acknowledge();
        } catch(Exception e) {
        	String msg = " 이체 정보를 저장 중에 문제가 발생했습니다.";
        	LOGGER.error(customer.getCstmId() + msg,e);
        } 
    }
}
