package com.modernbank.transfer.service;

import java.util.List;

import com.modernbank.transfer.domain.entity.TransferHistory;
import com.modernbank.transfer.domain.entity.TransferLimit;

public interface TransferService {
    public int createTransferHistory(TransferHistory transferHistory) throws Exception;
    public List<TransferHistory> retrieveTransferHistoryList(String cstmId) throws Exception;
    public int createTransferLimit(TransferLimit transferLimit) throws Exception;
    public TransferLimit retrieveEnableTransferLimit(String cstmId) throws Exception;
    public TransferLimit retrieveTransferLimit(String cstmId) throws Exception;
    public Long retrieveTotalTransferAmountPerDay(String cstmId) throws Exception;
    public TransferHistory transfer(TransferHistory transferHistory) throws Exception;
    public Boolean btobTransfer(TransferHistory transferHistory) throws Exception;
}
