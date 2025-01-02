package com.modernbank.b2bt.publisher;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import com.modernbank.b2bt.domain.TransferHistory;

@Component
public class B2BTransferResultProducer {
	
    private final Logger LOGGER = LoggerFactory.getLogger(B2BTransferResultProducer.class);
    
    @Autowired
    private KafkaTemplate<String, TransferHistory> transferResultKafkaTemplate;

    @Value(value = "${b2b.transfer.result.topic.name}")
    private String b2bTransferResultTopicName;

    public void sendB2BTransferResultMessage(TransferHistory transferResult) {
        CompletableFuture<SendResult<String, TransferHistory>> future = 
            transferResultKafkaTemplate.send(b2bTransferResultTopicName, transferResult);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                TransferHistory g = result.getProducerRecord().value();
                LOGGER.info("Sent message=[" + g.getWthdAcntNo() + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                // needed to do compensation transaction.
                LOGGER.error("Unable to send message=[" + transferResult.getWthdAcntNo() + "] due to : " + ex.getMessage(), ex);
            }
        });
    }
}