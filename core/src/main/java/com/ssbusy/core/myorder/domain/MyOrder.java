package com.ssbusy.core.myorder.domain;

import java.util.Date;

import org.broadleafcommerce.core.order.domain.Order;

public interface MyOrder  extends Order{
    
	public Date getDelieverDete();
	public void setDelieverDate(Date date);

	/**
	 * @return 是否订单项来自多个分仓
	 */
	public boolean isMultiFulfillmentLocations();
}
