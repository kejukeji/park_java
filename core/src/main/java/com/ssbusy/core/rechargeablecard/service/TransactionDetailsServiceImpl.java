package com.ssbusy.core.rechargeablecard.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ssbusy.core.rechargeablecard.dao.TransactionDetailsDao;
import com.ssbusy.core.rechargeablecard.domain.TransactionDetails;

@Service("ssbTransactionDetailsService")
public class TransactionDetailsServiceImpl implements TransactionDetailsService {
	@Resource(name = "ssbTransactionDetailsDao")
	protected TransactionDetailsDao transactionDetailsDao;

	@Override
	public List<TransactionDetails> getTransactionDetails(Long customerid) {
		// TODO Auto-generated method stub
		return transactionDetailsDao.readTransactionDetails(customerid);
	}
}
