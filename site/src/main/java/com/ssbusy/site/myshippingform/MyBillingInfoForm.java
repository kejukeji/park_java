package com.ssbusy.site.myshippingform;

import java.math.BigDecimal;

import org.broadleafcommerce.core.web.checkout.model.BillingInfoForm;

import com.ssbusy.core.domain.MyAddress;
import com.ssbusy.core.domain.MyAddressImpl;

public class MyBillingInfoForm extends BillingInfoForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private MyAddress myAddress = new MyAddressImpl();

	private BigDecimal bp_pay;
	private BigDecimal alipay;
	
	public MyAddress getMyAddress() {
		return myAddress;
	}

	public void setMyAddress(MyAddress myAddress) {
		this.myAddress = myAddress;
	}

	public BigDecimal getBp_pay() {
		return bp_pay;
	}

	public void setBp_pay(BigDecimal bp_pay) {
		this.bp_pay = bp_pay;
	}

	public BigDecimal getAlipay() {
		return alipay;
	}

	public void setAlipay(BigDecimal alipay) {
		this.alipay = alipay;
	}

}
