package com.ssbusy.admin.controller.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.core.order.domain.OrderItem;
import org.broadleafcommerce.core.payment.domain.PaymentInfo;

import com.ssbusy.core.domain.MyAddress;
import com.ssbusy.core.rechargeablecard.domain.RechargeableCard;

public class DeliveryForm {
	private Long orderId;
	private String emailAddress; 
	private MyAddress myAddress;
	private Money subTotal;
	private Date submitDate;
	private int quantitySum;
	private List<OrderItem> items;
	private String orderNumber;
	private Money total;
	private Money totalAdjustmentsValue;
	private String phone;
	private String detail;
	private BigDecimal shipping;
	private BigDecimal cashback;
	private Date deliveryDate;
	private String customerUserName;
    private Money codTotal;
    private Money bpTotal;
    private RechargeableCard rechargeableCard;
    private List<PaymentInfo> paymentInfos = new ArrayList<PaymentInfo>();;
    
	public String getCustomerUserName() {
		return customerUserName;
	}
	public void setCustomerUserName(String customerUserName) {
		this.customerUserName = customerUserName;
	}
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public BigDecimal getCashback() {
		return cashback;
	}
	public void setCashback(BigDecimal cashback) {
		this.cashback = cashback;
	}
	
	public BigDecimal getShipping() {
		return shipping;
	}
	public void setShipping(BigDecimal shipping) {
		this.shipping = shipping;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Money getTotal() {
		return total;
	}
	public void setTotal(Money total) {
		this.total = total;
	}
	public Money getTotalAdjustmentsValue() {
		return totalAdjustmentsValue;
	}
	public void setTotalAdjustmentsValue(Money totalAdjustmentsValue) {
		this.totalAdjustmentsValue = totalAdjustmentsValue;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public List<OrderItem> getItems() {
		return items;
	}
	public void setItems(List<OrderItem> items) {
		this.items = items;
	}
	public int getQuantitySum() {
		return quantitySum;
	}
	public void setQuantitySum(int quantitySum) {
		this.quantitySum = quantitySum;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public MyAddress getMyAddress() {
		return myAddress;
	}
	public void setMyAddress(MyAddress myAddress) {
		this.myAddress = myAddress;
	}
	public Money getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(Money subTotal) {
		this.subTotal = subTotal;
	}
	public Date getSubmitDate() {
		return submitDate;
	}
	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}
	public Money getCodTotal() {
		return codTotal;
	}
	public void setCodTotal(Money codTotal) {
		this.codTotal = codTotal;
	}
	public Money getBpTotal() {
		return bpTotal;
	}
	public void setBpTotal(Money bpTotal) {
		this.bpTotal = bpTotal;
	}
	public RechargeableCard getRechargeableCard() {
		return rechargeableCard;
	}
	public void setRechargeableCard(RechargeableCard rechargeableCard) {
		this.rechargeableCard = rechargeableCard;
	}
	public List<PaymentInfo> getPaymentInfos() {
		return paymentInfos;
	}
	public void setPaymentInfos(List<PaymentInfo> paymentInfos) {
		this.paymentInfos = paymentInfos;
	}
}
