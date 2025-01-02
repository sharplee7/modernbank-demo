package com.modernbank.transfer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.modernbank.transfer.domain.entity.TransferHistory;
import com.modernbank.transfer.domain.entity.TransferLimit;
import com.modernbank.transfer.domain.repository.TransferRepository;
import com.modernbank.transfer.exception.BusinessException;
import com.modernbank.transfer.exception.SystemException;
import com.modernbank.transfer.publisher.TransferProducer;
import com.modernbank.transfer.rest.account.entity.Account;
import com.modernbank.transfer.rest.account.entity.TransactionHistory;
import com.modernbank.transfer.rest.account.entity.TransactionResult;


@Service("transferService")
public class TransferServiceImpl implements TransferService {
	
    @Autowired
    TransferRepository transferRepository;
    
    @Autowired
    TransferProducer transferProducer;
    
    // @Autowired
    // AccountFeignClient accountFeignClient;
    
    // @Autowired
    // CustomerFeignClient customerFeignClient;
    
    @Autowired
    RestTemplate restTemplate;
    
    @Value("${account.api.url}")
    private String accountServiceUrl;
    
    @Value("${customer.api.url}")
    private String customerServiceUrl;

    @Override
    public int createTransferHistory(TransferHistory transferHistory) throws Exception {
    	// 이체 내역 생성
    	return transferRepository.insertTransferHistory(transferHistory);
    }

    // @Override
    // public List<TransferHistory> retrieveTransferHistoryList(String cstmId) throws Exception {
    //     // 고객 존재여부 조회
    //     if(customerFeignClient.existsCustomerId(cstmId) == false)
    //     	throw new BusinessException("존재하지 않는 아이디입니다.");
        
    // 	TransferHistory transferHistory = new TransferHistory();
    // 	transferHistory.setCstmId(cstmId);
    	
    //    	// 이체 이력 조회
    //     return transferRepository.selectTransferHistoryList(transferHistory);
    // }

    @Override
    public List<TransferHistory> retrieveTransferHistoryList(String cstmId) throws Exception {
        // 고객 존재여부 조회
        Boolean exists = restTemplate.getForObject(customerServiceUrl + "/exists/{cstmId}", Boolean.class, cstmId);
        if (exists == null || !exists) {
            throw new BusinessException("존재하지 않는 아이디입니다.");
        }
        
        TransferHistory transferHistory = new TransferHistory();
        transferHistory.setCstmId(cstmId);
        
        // 이체 이력 조회
        return transferRepository.selectTransferHistoryList(transferHistory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createTransferLimit(TransferLimit transferLimit) throws Exception {
    	// Kafka message send
    	transferProducer.sendUpdatingTansferLimitMessage(transferLimit);
    	// 이체 한도 생성
    	return transferRepository.insertTransferLimit(transferLimit);
    }

    @Override
    public TransferLimit retrieveTransferLimit(String cstmId) throws Exception {
    	TransferLimit transferLimit = new TransferLimit();
    	transferLimit.setCstmId(cstmId);
        
        // 이체 한도 조회
    	return transferRepository.selectTransferLimit(transferLimit);
    }

    @Override
    public Long retrieveTotalTransferAmountPerDay(String cstmId) throws Exception {
    	TransferLimit transferLimit = new TransferLimit();
    	transferLimit.setCstmId(cstmId);
        
        return transferRepository.selectTotalTransferAmountPerDay(transferLimit);
        
    }
    
	@Override
	public TransferLimit retrieveEnableTransferLimit(String cstmId) throws Exception {
		TransferLimit transferLimit = retrieveTransferLimit(cstmId);
        if(transferLimit == null)
            throw new BusinessException("이체 한도 조회를 실패하였습니다.");
        else {
        	// 일일 이체 합계 금액 조회
            Long totalTransferAmountPerDay = retrieveTotalTransferAmountPerDay(cstmId);
            if(totalTransferAmountPerDay < 0)
                throw new BusinessException("일일 이체 합계 금액 조회를 실패하였습니다.");
            else {
                Long remaingOneDayTransferLimit = transferLimit.getOneDyTrnfLmt() - totalTransferAmountPerDay;
                transferLimit.setOneDyTrnfLmt(remaingOneDayTransferLimit);
                return transferLimit;
            }
        }
	}

    /**
     * 이체
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TransferHistory transfer(TransferHistory transferHistory) throws Exception {
        String dpstAcntNo = transferHistory.getDpstAcntNo();
        String rcvCstmNm;
        String wthdAcntNo = transferHistory.getWthdAcntNo();
        Long trnfAmt = transferHistory.getTrnfAmt();
        String rcvMm = transferHistory.getRcvMm();
        String sndMm = transferHistory.getSndMm();
        String cstmId = transferHistory.getCstmId();
        int seq = retrieveMaxSeq(cstmId) + 1;
        
        // 입금 계좌 조회
        Account depositAccountInfo = restTemplate.getForObject(accountServiceUrl + "/{acntNo}", Account.class, dpstAcntNo);
        rcvCstmNm = depositAccountInfo.getCstmNm();
        
        transferHistory.setRcvCstmNm(rcvCstmNm);
        transferHistory.setSeq(seq);
        // 이체 구분 코드 : 1(당행)
        transferHistory.setDivCd("1");
        // 이체 상태 코드 : 1(이체 요청)
        transferHistory.setStsCd("1");
        // 이체내역 저장
        createTransferHistory(transferHistory);
        
        TransactionHistory transaction = new TransactionHistory();
        
        // 출금
        restTemplate.postForObject(accountServiceUrl + "/withdraw/", 
                transaction.builder()
                    .acntNo(wthdAcntNo)
                    .trnsAmt(trnfAmt)
                    .trnsBrnch(sndMm)
                    .build(), 
                TransactionResult.class);

        // 입금
        restTemplate.postForObject(accountServiceUrl + "/deposit/", 
                transaction.builder()
                    .acntNo(dpstAcntNo)
                    .trnsAmt(trnfAmt)
                    .trnsBrnch(rcvMm)
                    .build(), 
                TransactionResult.class);

        // 이체 상태 코드 : 3(이체 완료)
        transferHistory.setStsCd("3");
        // 이체 완료
        createTransferHistory(transferHistory);
        // CQRS
        transferProducer.sendCQRSTansferMessage(transferHistory);
            
        TransferHistory transferResult = new TransferHistory();
        transferResult.setWthdAcntNo(wthdAcntNo);
        transferResult.setDpstAcntNo(dpstAcntNo);
        transferResult.setRcvCstmNm(rcvCstmNm);
        transferResult.setTrnfAmt(trnfAmt);
        transferResult.setRcvMm(rcvMm);
        transferResult.setSndMm(sndMm);
        
        return transferResult;
    }

@Override
@Transactional(rollbackFor = Exception.class)
public Boolean btobTransfer(TransferHistory transfer) throws Exception {
    String wthdAcntNo = transfer.getWthdAcntNo();
    Long trnfAmt = transfer.getTrnfAmt();
    String sndMm = transfer.getSndMm();
    transfer.setRcvCstmNm("Amazon Web Services");
    String cstmId = transfer.getCstmId();
    int seq = retrieveMaxSeq(cstmId) + 1;
    
    transfer.setSeq(seq);
    // 이체 구분 코드 : 2(타행)
    transfer.setDivCd("2");
    // 이체 내역 저장
    createTransferHistory(transfer);
    
    TransactionHistory transaction = new TransactionHistory();
    TransactionResult withdrawResult;
    
    // 출금
    try {
        withdrawResult = restTemplate.postForObject(
            accountServiceUrl + "/withdraw/",
            transaction.builder()
                .acntNo(wthdAcntNo)
                .trnsAmt(trnfAmt)
                .trnsBrnch(sndMm)
                .build(),
            TransactionResult.class
        );
    } catch (Exception e) {
        throw new SystemException("출금 처리 중 오류가 발생했습니다: " + e.getMessage());
    }
    
    if (withdrawResult == null) {
        throw new SystemException("출금 결과를 받아오지 못했습니다.");
    }
    
    int wthdAcntSeq = withdrawResult.getSeq();
    transfer.setWthdAcntSeq(wthdAcntSeq);
    // 타행 입금
    transferProducer.sendB2BTansferMessage(transfer);
    // CQRS 
    transferProducer.sendCQRSTansferMessage(transfer);
        
    return true;
}
	
	private int retrieveMaxSeq(String cstmId) throws Exception {
		TransferHistory transferHistory = new TransferHistory();
		transferHistory.setCstmId(cstmId);
		
		return transferRepository.selectMaxSeq(transferHistory);
	}
}
