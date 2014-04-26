package com.ssbusy.site.myshippingform;

import org.broadleafcommerce.core.web.controller.account.CustomerAddressForm;

import com.ssbusy.core.domain.MyAddress;
import com.ssbusy.core.domain.MyAddressImpl;

public class MyCustomerAddressForm extends CustomerAddressForm{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected MyAddress myAddress = new MyAddressImpl();
	public MyAddress getMyAddress() {
		return myAddress;
	}
	public void setMyAddress(MyAddress myAddress) {
		this.myAddress = myAddress;
	}

}
