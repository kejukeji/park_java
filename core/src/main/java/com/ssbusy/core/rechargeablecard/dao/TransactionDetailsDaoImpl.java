package com.ssbusy.core.rechargeablecard.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.broadleafcommerce.common.persistence.EntityConfiguration;
import org.springframework.stereotype.Repository;

import com.ssbusy.core.account.service.BalanceChangeType;
import com.ssbusy.core.rechargeablecard.domain.TransactionDetails;

@Repository("ssbTransactionDetailsDao")
public class TransactionDetailsDaoImpl implements TransactionDetailsDao {
	@PersistenceContext(unitName = "blPU")
	protected EntityManager em;
	@Resource(name = "blEntityConfiguration")
	protected EntityConfiguration entityConfiguration;
	@SuppressWarnings("unchecked")
	@Override
	public List<TransactionDetails> readTransactionDetails(Long customerid) {
		// TODO Auto-generated method stub
		final Query query = em.createNamedQuery("SSB_READ_TRANSACTIONDETAILS");
		query.setParameter("customerid", customerid);
		List<TransactionDetails> transactionDetails = query.getResultList();
		return transactionDetails;
	}
	
	@Override
	public TransactionDetails createTransactionDeatils() {
		TransactionDetails o = (TransactionDetails) entityConfiguration
				.createEntityInstance("com.ssbusy.core.rechargeablecard.domain.TransactionDetails");
		o.setTransaction_id(null);
		return o;
	}

	@Override
	public void addtransactionDetails(Long customerid, BigDecimal value,
			BalanceChangeType type,Date time) {
		TransactionDetails transactionDetails=createTransactionDeatils();
		transactionDetails.setCustomer_id(customerid);
		transactionDetails.setTransaction_amount(value);
		transactionDetails.setTransaction_type(type);
		transactionDetails.setTransaction_time(time);
		em.merge(transactionDetails);
		//return null;
	}
	

}
