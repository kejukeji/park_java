package com.ssbusy.core.myfulfillmentgroup.domain;

import java.util.Date;

import org.broadleafcommerce.core.order.domain.FulfillmentGroup;

import com.ssbusy.core.domain.MyAddress;

public interface MyFulfillmentGroup extends FulfillmentGroup {
   
	public Date getDelieverDate();

	public void setDelieverDate(Date date);
	
	public MyAddress getMyAddress();
}
