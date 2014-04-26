package com.ssbusy.admin.Statistics.form;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.broadleafcommerce.core.order.domain.OrderItem;

public class AdminOrderInfoForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long cid;
	private String orderNumber;
	private String username;
	private BigDecimal total;
	private List<OrderItem> orderItems;

	public AdminOrderInfoForm(Long cid, String orderNumber, String username,
			BigDecimal total, List<OrderItem> orderItems) {
		super();
		this.cid = cid;
		this.orderNumber = orderNumber;
		this.username = username;
		this.total = total;
		this.orderItems = orderItems;
	}


	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

}
