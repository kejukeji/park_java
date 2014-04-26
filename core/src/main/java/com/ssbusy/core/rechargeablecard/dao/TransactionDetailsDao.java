package com.ssbusy.core.rechargeablecard.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.ssbusy.core.account.service.BalanceChangeType;
import com.ssbusy.core.rechargeablecard.domain.TransactionDetails;


public interface TransactionDetailsDao {
	public List<TransactionDetails> readTransactionDetails(Long customerid);
	public TransactionDetails createTransactionDeatils();
	public void addtransactionDetails(Long customerid, BigDecimal value,BalanceChangeType type,Date time);
}
