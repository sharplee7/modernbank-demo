package com.modernbank.transfer.subscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.modernbank.transfer.domain.entity.TransferHistory;
import com.modernbank.transfer.exception.SystemException;
import com.modernbank.transfer.publisher.TransferProducer;
import com.modernbank.transfer.rest.account.entity.TransactionHistory;
import com.modernbank.transfer.service.TransferService;

import jakarta.annotation.Resource;

@Component
public class B2BTransferResultConsumer {
    
    private final Logger LOGGER = LoggerFactory.getLogger(B2BTransferResultConsumer.class);
    
    @Resource(name = "transferService")
    private TransferService transferService;
    
    @Autowired
    TransferProducer transferProducer;
    
    @Autowired
    RestTemplate restTemplate;
    
    @Value("${account.api.url}")
    private String accountServiceUrl;
    
    @KafkaListener(topics = "${b2b.transfer.result.topic.name}", containerFactory = "b2bTransferResultKafkaListenerContainerFactory")
    public void b2bTransferResultListener(TransferHistory transferResult, Acknowledgment ack) throws Exception {
        LOGGER.info("Received Bank-To-Bank transfer result message: " + transferResult.getWthdAcntNo());
        
        String statusCode = transferResult.getStsCd();
        
        try {
            if("2".equals(statusCode)) {
                String wthdAcntNo = transferResult.getWthdAcntNo();
                int wthdAcntSeq = transferResult.getWthdAcntSeq();

                TransactionHistory transactionHistory = TransactionHistory.builder()
                    .acntNo(wthdAcntNo)
                    .seq(wthdAcntSeq)
                    .build();

                restTemplate.postForObject(
                    accountServiceUrl + "/withdraw/cancel/",
                    transactionHistory,
                    Integer.class
                );
            }
            
            transferService.createTransferHistory(transferResult);
            
            // CQRS
            transferProducer.sendCQRSTansferMessage(transferResult);
            ack.acknowledge(); // 모든 CRUD 작업이 완료되어야만 kafka의 read off-set 값을 변경하도록 합니다.
        } catch (Exception e) {
            String msg = "시스템에 예상치 못한 문제가 발생했습니다";
            LOGGER.error(msg, e);
            throw new SystemException(msg);
        } 
    }

}
