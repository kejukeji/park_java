package com.ssbusy.core.christmas.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SSB_CHRISTMAS_CARD")
public class ChristmasCardImpl implements ChristmasCard {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="CHRISTMAS_CARD_ID")
	private Long id;
	@Column(name="CUSTOMER_ID")
	private Long customerId;
	@Column(name="SIGN_DATE")
	private int signDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public int getSignDate() {
		return signDate;
	}

	public void setSignDate(int signDate) {
		this.signDate = signDate;
	}

}
