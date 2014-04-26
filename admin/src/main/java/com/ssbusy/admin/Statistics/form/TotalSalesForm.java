package com.ssbusy.admin.Statistics.form;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TotalSalesForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Date submitDate;
	private BigDecimal total;
	private BigDecimal codPay;
	private BigDecimal aliPay;
	private BigDecimal bpPay;
	public TotalSalesForm(Date submitDate, BigDecimal total, BigDecimal codPay,
			BigDecimal aliPay, BigDecimal bpPay) {
		super();
		this.submitDate = submitDate;
		this.total = total;
		this.codPay = codPay;
		this.aliPay = aliPay;
		this.bpPay = bpPay;
	}
	public Date getSubmitDate() {
		return submitDate;
	}
	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public BigDecimal getCodPay() {
		return codPay;
	}
	public void setCodPay(BigDecimal codPay) {
		this.codPay = codPay;
	}
	public BigDecimal getAliPay() {
		return aliPay;
	}
	public void setAliPay(BigDecimal aliPay) {
		this.aliPay = aliPay;
	}
	public BigDecimal getBpPay() {
		return bpPay;
	}
	public void setBpPay(BigDecimal bpPay) {
		this.bpPay = bpPay;
	}
}
