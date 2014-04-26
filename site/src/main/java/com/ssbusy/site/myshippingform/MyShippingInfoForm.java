package com.ssbusy.site.myshippingform;

import java.math.BigDecimal;

import org.broadleafcommerce.core.web.checkout.model.ShippingInfoForm;
import org.broadleafcommerce.profile.core.domain.Address;

import com.ssbusy.core.domain.MyAddress;
import com.ssbusy.core.domain.MyAddressImpl;

/**
 * 
 * {@link #getAddress()}废弃，使用{@link #getMyAddress()}
 * 
 */
public class MyShippingInfoForm extends ShippingInfoForm {

	private static final long serialVersionUID = 1L;

	private String paymentMethod;

	protected MyAddress myAddress = new MyAddressImpl();

	private BigDecimal bp_pay;
	private BigDecimal alipay;

	public MyAddress getMyAddress() {
		return myAddress;
	}

	public void setMyAddress(MyAddress myAddress) {
		this.myAddress = myAddress;
	}

	/**
	 * @deprecated {@link #getAddress()}废弃，使用{@link #getMyAddress()}
	 */
	@Override
	@Deprecated
	public Address getAddress() {
		return super.getAddress();
	}

	/**
	 * @deprecated {@link #getAddress()}废弃，使用{@link #getMyAddress()}
	 */
	@Override
	@Deprecated
	public void setAddress(Address address) {
		super.setAddress(address);
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

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

}
