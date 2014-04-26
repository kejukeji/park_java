package com.ssbusy.admin.Statistics.form;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductSalesForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String productName;
	private BigDecimal avgSalesprice;
	private BigDecimal avgInvoicingPrice;
	private BigDecimal totalPrice;
	private Integer saleQuantity;
	private BigDecimal allTotalPrice;
	
	public ProductSalesForm(String productName, BigDecimal avgSalesprice,
			BigDecimal avgInvoicingPrice, BigDecimal totalPrice,
			Integer saleQuantity, BigDecimal allTotalPrice) {
		super();
		this.productName = productName;
		this.avgSalesprice = avgSalesprice;
		this.avgInvoicingPrice = avgInvoicingPrice;
		this.totalPrice = totalPrice;
		this.saleQuantity = saleQuantity;
		this.allTotalPrice = allTotalPrice;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public BigDecimal getAvgSalesprice() {
		return avgSalesprice;
	}
	public void setAvgSalesprice(BigDecimal avgSalesprice) {
		this.avgSalesprice = avgSalesprice;
	}
	public BigDecimal getAvgInvoicingPrice() {
		return avgInvoicingPrice;
	}
	public void setAvgInvoicingPrice(BigDecimal avgInvoicingPrice) {
		this.avgInvoicingPrice = avgInvoicingPrice;
	}
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Integer getSaleQuantity() {
		return saleQuantity;
	}
	public void setSaleQuantity(Integer saleQuantity) {
		this.saleQuantity = saleQuantity;
	}
	public BigDecimal getAllTotalPrice() {
		return allTotalPrice;
	}
	public void setAllTotalPrice(BigDecimal allTotalPrice) {
		this.allTotalPrice = allTotalPrice;
	}

}
