package com.modernbank.transfer.service;

import java.util.List;

import com.modernbank.transfer.domain.entity.TransferHistory;
import com.modernbank.transfer.domain.entity.TransferLimit;
import com.modernbank.transfer.domain.repository.TransferRepository;
import com.modernbank.transfer.exception.BusinessException;
import com.modernbank.transfer.exception.SystemException;
import com.modernbank.transfer.publisher.TransferProducer;
import com.modernbank.transfer.rest.account.entity.Account;
import com.modernbank.transfer.rest.account.entity.TransactionHistory;
import com.modernbank.transfer.rest.account.entity.TransactionResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


@Service("transferService")
public class TransferServiceImpl implements TransferService {
	
    @Autowired
    TransferRepository transferRepository;
    
    @Autowired
    TransferProducer transferProducer;
    
    @Autowired
    RestTemplate restTemplate;
    
    @Value("${account.api.url}")
    private String accountServiceUrl;
    
    @Value("${customer.api.url}")
    private String customerServiceUrl;

    @Override
    public int createTransferHistory(TransferHistory transferHistory) throws Exception {
    	// Create transfer history
    	return transferRepository.insertTransferHistory(transferHistory);
    }

    @Override
    public List<TransferHistory> retrieveTransferHistoryList(String cstmId) throws Exception {
        // Check if customer exists
        Boolean exists = restTemplate.getForObject(customerServiceUrl + "/{cstmId}/exists", Boolean.class, cstmId);
        if (exists == null || !exists) {
            throw new BusinessException("ID does not exist.");
        }
        
        TransferHistory transferHistory = new TransferHistory();
        transferHistory.setCstmId(cstmId);
        
        // Retrieve transfer history
        return transferRepository.selectTransferHistoryList(transferHistory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createTransferLimit(TransferLimit transferLimit) throws Exception {
    	// Kafka message send
    	transferProducer.sendUpdatingTansferLimitMessage(transferLimit);
    	// Create transfer limit
    	return transferRepository.insertTransferLimit(transferLimit);
    }

    @Override
    public TransferLimit retrieveTransferLimit(String cstmId) throws Exception {
    	TransferLimit transferLimit = new TransferLimit();
    	transferLimit.setCstmId(cstmId);
        
        // Retrieve transfer limit
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
            throw new BusinessException("Failed to retrieve transfer limit.");
        else {
        	// Retrieve total transfer amount per day
            Long totalTransferAmountPerDay = retrieveTotalTransferAmountPerDay(cstmId);
            if(totalTransferAmountPerDay < 0)
                throw new BusinessException("Failed to retrieve total transfer amount per day.");
            else {
                Long remaingOneDayTransferLimit = transferLimit.getOneDyTrnfLmt() - totalTransferAmountPerDay;
                transferLimit.setOneDyTrnfLmt(remaingOneDayTransferLimit);
                return transferLimit;
            }
        }
	}

    /**
     * Transfer
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
        
        // Retrieve deposit account
        Account depositAccountInfo = restTemplate.getForObject(accountServiceUrl + "/{acntNo}", Account.class, dpstAcntNo);
        rcvCstmNm = depositAccountInfo.getCstmNm();
        
        transferHistory.setRcvCstmNm(rcvCstmNm);
        transferHistory.setSeq(seq);
        // Transfer division code : 1(same bank)
        transferHistory.setDivCd("1");
        // Transfer status code : 1(transfer request)
        transferHistory.setStsCd("1");
        // Save transfer history
        createTransferHistory(transferHistory);
        
        TransactionHistory transaction = new TransactionHistory();
        
        // Withdrawal
        restTemplate.postForObject(accountServiceUrl + "/withdraws/", 
                transaction.builder()
                    .acntNo(wthdAcntNo)
                    .trnsAmt(trnfAmt)
                    .trnsBrnch(sndMm)
                    .build(), 
                TransactionResult.class);

        // Deposit
        restTemplate.postForObject(accountServiceUrl + "/deposits/", 
                transaction.builder()
                    .acntNo(dpstAcntNo)
                    .trnsAmt(trnfAmt)
                    .trnsBrnch(rcvMm)
                    .build(), 
                TransactionResult.class);

        // Transfer status code : 3(transfer complete)
        transferHistory.setStsCd("3");
        // Complete transfer
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
    // Transfer division code : 2(other bank)
    transfer.setDivCd("2");
    // Save transfer history
    createTransferHistory(transfer);
    
    TransactionHistory transaction = new TransactionHistory();
    TransactionResult withdrawResult;
    
    // Withdrawal
    try {
        withdrawResult = restTemplate.postForObject(
            accountServiceUrl + "/withdraws/",
            transaction.builder()
                .acntNo(wthdAcntNo)
                .trnsAmt(trnfAmt)
                .trnsBrnch(sndMm)
                .build(),
            TransactionResult.class
        );
    } catch (Exception e) {
        throw new SystemException("An error occurred during withdrawal processing: " + e.getMessage());
    }
    
    if (withdrawResult == null) {
        throw new SystemException("Failed to receive withdrawal result.");
    }
    
    int wthdAcntSeq = withdrawResult.getSeq();
    transfer.setWthdAcntSeq(wthdAcntSeq);
    // Other bank deposit
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