package com.modernbank.transfer.publisher;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import com.modernbank.transfer.domain.entity.TransferHistory;
import com.modernbank.transfer.domain.entity.TransferLimit;
import com.modernbank.transfer.exception.SystemException;

@Component
public class TransferProducer {
    
    private final Logger LOGGER = LoggerFactory.getLogger(TransferProducer.class);
    
    @Autowired
    private KafkaTemplate<String, TransferHistory> transferKafkaTemplate;
    
    @Autowired
    private KafkaTemplate<String, TransferLimit> transferLimitKafkaTemplate;

    @Value(value = "${b2b.transfer.topic.name}")
    private String b2bTransferTopicName;
    
    @Value(value = "${updating.transfer.limit.topic.name}")
    private String updatingTransferLimitTopicName;
    
    @Value(value = "${transfer.topic.name}")
    private String transferTopicName;

    public void sendB2BTansferMessage(TransferHistory transfer) {
        CompletableFuture<SendResult<String, TransferHistory>> future = transferKafkaTemplate.send(b2bTransferTopicName, transfer);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                TransferHistory g = result.getProducerRecord().value();
                LOGGER.info("Sent message=[" + g.getCstmId() + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                LOGGER.error("Unable to send message=[" + transfer.getCstmId() + "] due to : " + ex.getMessage());
                throw new SystemException("Kafka 데이터 전송 에러");
            }
        });
    }
    
    public void sendUpdatingTansferLimitMessage(TransferLimit transferLimit) {
        CompletableFuture<SendResult<String, TransferLimit>> future = transferLimitKafkaTemplate.send(updatingTransferLimitTopicName, transferLimit);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                TransferLimit g = result.getProducerRecord().value();
                LOGGER.info("Sent message=[" + g.getCstmId() + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                LOGGER.error("Unable to send message=[" + transferLimit.getCstmId() + "] due to : " + ex.getMessage());
                throw new SystemException("Kafka 데이터 전송 에러");
            }
        });
    }
    
    public void sendCQRSTansferMessage(TransferHistory transfer) {
        CompletableFuture<SendResult<String, TransferHistory>> future = transferKafkaTemplate.send(transferTopicName, transfer);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                TransferHistory g = result.getProducerRecord().value();
                LOGGER.info("Sent message=[" + g.getCstmId() + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                LOGGER.error("Unable to send message=[" + transfer.getCstmId() + "] due to : " + ex.getMessage());
                throw new SystemException("Kafka 데이터 전송 에러");
            }
        });
    }
}