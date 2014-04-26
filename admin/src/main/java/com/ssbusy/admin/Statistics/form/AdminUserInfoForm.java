package com.ssbusy.admin.Statistics.form;

import java.io.Serializable;
import java.math.BigDecimal;

public class AdminUserInfoForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String firstname;
	private String buyCount;
	private BigDecimal totalBuy;
	private int totalIntegral;

	public Long getId() {
		return id;
	}
	
	public AdminUserInfoForm(Long id, String firstname, String buyCount,
			BigDecimal totalBuy, int totalIntegral) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.buyCount = buyCount;
		this.totalBuy = totalBuy;
		this.totalIntegral = totalIntegral;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(String buyCount) {
		this.buyCount = buyCount;
	}

	public BigDecimal getTotalBuy() {
		return totalBuy;
	}

	public void setTotalBuy(BigDecimal totalBuy) {
		this.totalBuy = totalBuy;
	}

	public int getTotalIntegral() {
		return totalIntegral;
	}

	public void setTotalIntegral(int totalIntegral) {
		this.totalIntegral = totalIntegral;
	}
}
