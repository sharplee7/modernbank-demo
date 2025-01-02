package com.modernbank.transfer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.modernbank.transfer.domain.entity.TransferHistory;
import com.modernbank.transfer.domain.entity.TransferLimit;
import com.modernbank.transfer.service.TransferService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;

@RestController
public class TransferController {

    @Autowired
    @Resource(name = "transferService")
    private TransferService transferService;
    
    @Operation(summary = "당행이체", method = "POST", description = "당행이체")
    @RequestMapping(method = RequestMethod.POST, path = "/")
    public TransferHistory transfer(@RequestBody TransferHistory input) throws Exception{
        return transferService.transfer(input);
    }
    
    /*타행 이체*/
    @Operation(summary = "타행이체", method = "POST", description = "타행이체")
    @RequestMapping(method = RequestMethod.POST, path = "/b2b/")
    public Boolean btobTransfer(@RequestBody TransferHistory input) throws Exception{
    	return transferService.btobTransfer(input);
    }

    @Operation(summary = "이체이력조회", method = "GET", description = "이체이력조회")
    @RequestMapping(method = RequestMethod.GET, path = "/transfer-history/{cstmId}")
    public List<TransferHistory> retrieveTransferHistoryList(@PathVariable(name = "cstmId") String cstmId) throws Exception{
        List<TransferHistory> transferHistory = transferService.retrieveTransferHistoryList(cstmId);
        return transferHistory;
    }

    @Operation(summary = "이체한도등록", method = "POST", description = "이체한도등록")
    @RequestMapping(method = RequestMethod.POST, path = "/transfer-limit/")
    public Integer createTransferLimit(@RequestBody TransferLimit input) throws Exception{
        return  transferService.createTransferLimit(input);
    }

    @Operation(summary = "이체한도조회", method = "GET", description = "이체한도조회")
    @RequestMapping(method = RequestMethod.GET, path = "/transfer-limit/{cstmId}")
    public TransferLimit retrieveTransferLimit(@PathVariable(name = "cstmId") String cstmId) throws Exception{
        return transferService.retrieveTransferLimit(cstmId);
    }

    @Operation(summary = "이체가능한도조회", method = "GET", description = "이체가능한도조회")
    @RequestMapping(method = RequestMethod.GET, path = "/transfer-limit/enable/{cstmId}")
    public TransferLimit retrieveEnableTransferLimit(@PathVariable(name = "cstmId") String cstmId) throws Exception{
        return  transferService.retrieveEnableTransferLimit(cstmId);
    }
}
