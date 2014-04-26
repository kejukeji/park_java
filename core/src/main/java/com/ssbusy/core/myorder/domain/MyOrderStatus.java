package com.ssbusy.core.myorder.domain;

import org.broadleafcommerce.core.order.service.type.OrderStatus;

public class MyOrderStatus extends OrderStatus {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final OrderStatus DELIVERY = new OrderStatus("DELIVERY", "Delivery");
	public static final OrderStatus COMPLETED = new OrderStatus("COMPLETED","Completed");
	public static final OrderStatus DISTRIBUTED = new OrderStatus("DISTRIBUTED","Distributed");
	public static final OrderStatus DISTRIBUTING = new OrderStatus("DISTRIBUTING","Distributing");

}
