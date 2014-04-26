package com.ssbusy.core.myorder.service;

import java.util.Date;
import java.util.List;

import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.domain.OrderItem;
import org.broadleafcommerce.core.order.service.OrderService;
import org.broadleafcommerce.core.order.service.type.OrderStatus;
import org.broadleafcommerce.core.pricing.service.exception.PricingException;
import org.broadleafcommerce.inventory.domain.FulfillmentLocation;
import org.broadleafcommerce.inventory.exception.ConcurrentInventoryModificationException;

import com.ssbusy.core.account.domain.MyCustomer;
import com.ssbusy.core.myorder.domain.MyOrder;

public interface MyOrderService extends OrderService {

	public Long countOrderByTime(Date lastTime,
			FulfillmentLocation fulfillmentLocation);

	/**
	 * @param orderId
	 * @return 根据orderId返回一个Order对象。
	 */
	public Order loadOrderByOrderId(long orderId);

	public List<Order> findOrdersForProcess();

	public void processOrderStatus(Long[] orderIds, OrderStatus status);

	public List<Order> getOrderListByOrderIdsAndAdminUserId(Long[] orderIds,
			Long adminUserId);

	/**
	 * 列出指定状态下的订单，且OrderItem.location跟指定location相关；无一订单项与指定location相关的订单不列出
	 * 
	 * @param status
	 * @param fulfillmentLocationId
	 * @return
	 */
	public List<MyOrder> getOrderListByStatus(OrderStatus status,
			Long fulfillmentLocationId);

	public void setOrderUpdateBy(Long[] orderIds, Long adminUserId);

	public List<Order> getOrderListByStatusAndAdminUserId(OrderStatus status,
			Long adminUserId);

	/**
	 * @param order
	 * @param myCustomer
	 * @return <code>null</code> 表示非法更新；
	 *         <code>true</code> 表示取消订单成功；
	 *         <code>false</code> 表示不满足取消订单条件；
	 * @throws PricingException
	 * @throws ConcurrentInventoryModificationException
	 */
	public Boolean cancelMyOrder(Order order, MyCustomer myCustomer)
			throws PricingException, ConcurrentInventoryModificationException;

	/**
	 * @param orderItem
	 * @param myCustomer
	 * @return
	 * @throws ConcurrentInventoryModificationException
	 */
	public Boolean cancelOrderItem(OrderItem orderItem, MyCustomer myCustomer,
			long quantity) throws ConcurrentInventoryModificationException;

	/**
	 * @return 返回时submitted状态之后的所有订单
	 */
	public List<Order> getAllOrdersAfterSubmitted(Long customerId);

	public List<Order> getOrderbyIds(Long[] orderIds);

	public List<Order> getAllSubmittedInTime(Long customerID, Date begintime,
			Date endtime);

}
