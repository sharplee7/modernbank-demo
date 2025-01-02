package com.modernbank.b2bt.subscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.modernbank.b2bt.domain.TransferHistory;
import com.modernbank.b2bt.publisher.B2BTransferResultProducer;

@Component
public class B2BTransferConsumer {
    private final Logger LOGGER = LoggerFactory.getLogger(B2BTransferConsumer.class);
    
    @Autowired
    B2BTransferResultProducer b2btransferResultProducer;
    
    @KafkaListener(topics = "${b2b.transfer.topic.name}", containerFactory = "b2bTransferKafkaListenerContainerFactory")
    public void b2bTransferListener(TransferHistory transfer, Acknowledgment ack) {
        LOGGER.info("Recieved Bank-To-Bank message: " + transfer.getWthdAcntNo() + ":" +transfer.getWthdAcntSeq());
		try {
            //타행이체결과 send
            b2btransferResultProducer.sendB2BTransferResultMessage(transfer);
            ack.acknowledge();
        }catch(Exception e) {
        	String msg = " 이체 정보 이력 저장에 문제가 발생했습니다.";
            LOGGER.error(transfer.getWthdAcntNo() + msg,e);
//            ack.nack(1000 * 5); 리스너 재 실행 시간 지정
        }
    }
}
