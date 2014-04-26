package com.ssbusy.core.integral.domain;

import java.util.Date;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "SSB_INTEGRAL")
public class IntegralImpl implements Integral {
	
	public Long getId() {
		return id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "INTEGRALCHANGE_ID")
	protected Long id;
	
	@Column(name = "CUSTOMER_ID")
	private Long customerId;
	
	@Column(name = "CHANGE_TYPE")
	private String changeType;
	
	@Column(name = "CHANGE_QUANTITY")
	private int changeQuantity;
	
	@Column(name = "CHANGE_DATE")
	private Date changeDate;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public int getChangeQuantity() {
		return changeQuantity;
	}

	public void setChangeQuantity(int changeQuantity) {
		this.changeQuantity = changeQuantity;
	}

	public Date getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}
	
}
