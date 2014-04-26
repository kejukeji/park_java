package com.ssbusy.core.rechargeablecard.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.ssbusy.core.account.service.BalanceChangeType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="SB_TRANSATION_DETAILS")
public class TransactionDetailsImpl implements TransactionDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TRANSACTION_ID")
	protected Long id;
	
	@Column(name = "CUSTOMER_ID")
	private Long customerId;
	
	@Column(name="TRANSACTION_TYPE")
	private String transaction_type;
	
	@Column(name="TRANSACTION_AMOUNT")
	private BigDecimal transaction_amount;
	
	@Column(name="TRANSACTION_TIME")
	private Date transaction_time;
	
	@Override
	public Long getTransaction_id() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void setTransaction_id(Long id) {
		// TODO Auto-generated method stub
		this.id=id;
	}

	@Override
	public BalanceChangeType getTransaction_type() {
		// TODO Auto-generated method stub
		return BalanceChangeType.getInstance(transaction_type);
	}

	@Override
	public void setTransaction_type(BalanceChangeType type) {
		// TODO Auto-generated method stub
		this.transaction_type=type.getType();
	}

	@Override
	public BigDecimal getTransaction_amount() {
		// TODO Auto-generated method stub
		return transaction_amount;
	}

	@Override
	public void setTransaction_amount(BigDecimal transaction_amount) {
		// TODO Auto-generated method stub
		this.transaction_amount=transaction_amount;
	}

	@Override
	public Date getTransaction_time() {
		// TODO Auto-generated method stub
		return transaction_time;
	}

	@Override
	public void setTransaction_time(Date transaction_time) {
		// TODO Auto-generated method stub
		this.transaction_time=transaction_time;
	}

	@Override
	public Long getCustomer_id() {
		// TODO Auto-generated method stub
		return customerId;
	}

	@Override
	public void setCustomer_id(Long customerId) {
		// TODO Auto-generated method stub
		this.customerId=customerId;
	}

}
