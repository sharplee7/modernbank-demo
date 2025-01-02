package com.modernbank.account.domain.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TransactionHistory {

    private String acntNo;
    private int seq;
    private String divCd; //거래구분코드 D:입금, W:출금
    private String stsCd; //거래상태 1:성공, 2:실패, 3:진행중
    private Long trnsAmt; //거래금액
    private Long acntBlnc; //계좌잔액
    private String trnsBrnch; //거래지점
    private String trnsDtm; //거래일시

    @Builder
    public TransactionHistory(String acntNo, int seq, String divCd, String stsCd, Long trnsAmt, Long acntBlnc, String trnsBrnch, String trnsDtm) {
        this.acntNo = acntNo;
        this.seq = seq;
        this.divCd = divCd;
        this.stsCd = stsCd;
        this.trnsAmt = trnsAmt;
        this.acntBlnc = acntBlnc;
        this.trnsBrnch = trnsBrnch;
        this.trnsDtm = trnsDtm;
    }

    public static TransactionHistory ofAcntNo(String acntNo) {
        return TransactionHistory.builder().acntNo(acntNo).build();
    }
}
