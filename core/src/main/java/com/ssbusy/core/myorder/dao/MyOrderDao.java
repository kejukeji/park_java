package com.ssbusy.core.myorder.dao;

import java.util.Date;
import java.util.List;

import org.broadleafcommerce.core.order.dao.OrderDao;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.service.type.OrderStatus;
import org.broadleafcommerce.inventory.domain.FulfillmentLocation;

import com.ssbusy.core.myorder.domain.MyOrder;

public interface MyOrderDao extends OrderDao {

	public Long countOrderByTime(Date lastTime,
			FulfillmentLocation fulfillmentLocation);

	public List<Order> findOrderForProcess();

	public void setOrderStatus(Long[] orderIds, OrderStatus status);

	/**
	 * @param OrderId
	 * @return 根据orderId返回一个Order对象。
	 */
	public Order loadOrderByOrderId(Long orderId);

	public List<Order> getOrderListByOrderIds(Long[] orderIds, Long adminUserId);

	public List<MyOrder> getOrderListByStatus(OrderStatus status,
			Long fulfillmentLocationId);

	public void setOrderUpdateBy(Long[] orderIds, Long adminUserId);

	public List<Order> getOrderListByStatusAndAdminUserId(OrderStatus status,
			Long adminUserId);

	public List<Order> getAllOrdersAfterSubmitted(Long customerId);

	public List<Order> getAllOrdersAfterSubmittedIntime(Long customerid,
			Date beforetime, Date aftertime);

	public List<Order> getOrderbyIds(Long[] orderIds);

}
