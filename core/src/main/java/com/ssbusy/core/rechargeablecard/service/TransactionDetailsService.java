package com.ssbusy.core.rechargeablecard.service;

import java.util.List;

import com.ssbusy.core.rechargeablecard.domain.TransactionDetails;

public interface TransactionDetailsService {
	List<TransactionDetails> getTransactionDetails(Long customerid);
}
