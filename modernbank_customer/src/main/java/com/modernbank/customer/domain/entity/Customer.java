package com.modernbank.customer.domain.entity;

import java.util.ArrayList;
import java.util.List;

import com.modernbank.customer.rest.account.entity.Account;

// import lombok.Builder;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// @Builder
// @NoArgsConstructor
// @Data
public class Customer {
	private String cstmId;
	private String cstmNm;
	private String cstmAge;
	private String cstmGnd;
	private String cstmPn;
	private String cstmAdr;

	public Customer(String cstmId, String cstmNm, String cstmAge, String cstmGnd, String cstmPn, String cstmAdr) {
		setCstmId(cstmId);
		setCstmNm(cstmNm);
		setCstmAge(cstmAge);
		setCstmGnd(cstmGnd);
		setCstmPn(cstmPn);
		setCstmAdr(cstmAdr);
	}

	public Customer() {
	}

	public String getCstmId() {
		return cstmId;
	}

	public String getCstmNm() {
		return cstmNm;
	}

	public String getCstmAge() {
		return cstmAge;
	}

	public String getCstmGnd() {
		return cstmGnd;
	}

	public String getCstmPn() {
		return cstmPn;
	}

	public String getCstmAdr() {
		return cstmAdr;
	}

	public Long getOneTmTrnfLmt() {
		return oneTmTrnfLmt;
	}

	public Long getOneDyTrnfLmt() {
		return oneDyTrnfLmt;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setCstmId(String cstmId) {
		this.cstmId = cstmId;
	}

	public void setCstmNm(String cstmNm) {
		this.cstmNm = cstmNm;
	}

	public void setCstmAge(String cstmAge) {
		this.cstmAge = cstmAge;
	}

	public void setCstmGnd(String cstmGnd) {
		this.cstmGnd = cstmGnd;
	}

	public void setCstmPn(String cstmPn) {
		this.cstmPn = cstmPn;
	}

	public void setCstmAdr(String cstmAdr) {
		this.cstmAdr = cstmAdr;
	}

	public void setOneTmTrnfLmt(Long oneTmTrnfLmt) {
		this.oneTmTrnfLmt = oneTmTrnfLmt;
	}

	public void setOneDyTrnfLmt(Long oneDyTrnfLmt) {
		this.oneDyTrnfLmt = oneDyTrnfLmt;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	/* 이체 한도 정보 */
	private Long oneTmTrnfLmt;
	private Long oneDyTrnfLmt;

	/* 계좌 목록 */

	private List<Account> accounts = new ArrayList<>();

	public void addAllAccounts(List<Account> accounts) {
		this.accounts.addAll(accounts);
	}

	public Customer(String cstmId, String cstmNm, String cstmAge, String cstmGnd, String cstmPn, String cstmAdr,
			Long oneTmTrnfLmt, Long oneDyTrnfLmt) {
		super();
		this.cstmId = cstmId;
		this.cstmNm = cstmNm;
		this.cstmAge = cstmAge;
		this.cstmGnd = cstmGnd;
		this.cstmPn = cstmPn;
		this.cstmAdr = cstmAdr;
		this.oneTmTrnfLmt = oneTmTrnfLmt;
		this.oneDyTrnfLmt = oneDyTrnfLmt;
	}
}
