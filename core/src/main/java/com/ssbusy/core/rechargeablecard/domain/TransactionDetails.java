package com.ssbusy.core.rechargeablecard.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.ssbusy.core.account.service.BalanceChangeType;

public interface TransactionDetails {
	public Long getTransaction_id();

	public void setTransaction_id(Long id);

	public BalanceChangeType getTransaction_type();

	public void setTransaction_type(BalanceChangeType type);

	public BigDecimal getTransaction_amount();

	public void setTransaction_amount(BigDecimal transaction_amount);

	public Date getTransaction_time();

	public void setTransaction_time(Date Transaction_time);
	
	public Long getCustomer_id();
	
	public void setCustomer_id(Long customerId);
}
