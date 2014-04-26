package com.ssbusy.core.myorder.dao;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.broadleafcommerce.core.order.dao.OrderDaoImpl;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.domain.OrderImpl;
import org.broadleafcommerce.core.order.service.type.OrderStatus;
import org.broadleafcommerce.inventory.domain.FulfillmentLocation;
import org.springframework.stereotype.Repository;

import com.ssbusy.core.myorder.domain.MyOrder;

@Repository("ssbMyOrderDao")
public class MyOrderDaoImpl extends OrderDaoImpl implements MyOrderDao {

	@Override
	public Long countOrderByTime(Date lastTime,
			FulfillmentLocation fulfillmentLocation) {

		final Query query = em
				.createNamedQuery("BC_READ_ORDER_BY_TIME_AND_FULFILLMENTLOCATION");
		query.setParameter("lastTime", lastTime);
		query.setParameter("fulfillmentLocationId", fulfillmentLocation.getId());
		return (Long) query.getSingleResult();
	}

	@Override
	public List<Order> findOrderForProcess() {

		final Query query = em.createNamedQuery("SSB_READ_ORDER_FOR_PROCESS");
		@SuppressWarnings("unchecked")
		List<Order> orders = query.getResultList();
		return orders;

	}

	@Override
	public void setOrderStatus(Long[] orderIds, OrderStatus status) {
		final Query query = em
				.createNamedQuery("SSB_UPDATE_ORDERSTATUS_BY_ORDERIDS");
		List<Long> orderIdss = Arrays.asList(orderIds);
		query.setParameter("orderIds", orderIdss);
		query.setParameter("status", status.getType());
		query.executeUpdate();
	}

	@Override
	public Order loadOrderByOrderId(Long orderId) {

		return em.find(OrderImpl.class, orderId);
	}

	@Override
	public List<Order> getOrderListByOrderIds(Long[] orderIds, Long adminUserId) {
		List<Long> orderIdss = Arrays.asList(orderIds);
		final Query query = em
				.createNamedQuery("SSB_READ_ORDER_BY_ORDERIDS_AND_ADMINUSERID");
		query.setParameter("orderIds", orderIdss);
		query.setParameter("adminUserId", adminUserId);
		@SuppressWarnings("unchecked")
		List<Order> orders = query.getResultList();
		return orders;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<MyOrder> getOrderListByStatus(OrderStatus status,
			Long fulfillmentLocationId) {
		final Query query = em.createNamedQuery("SSB_READ_ORDER_BY_STATUS");
		query.setParameter("status", status.getType());
		query.setParameter("fulfillmentLocationId", fulfillmentLocationId);
		List<MyOrder> orders = query.getResultList();
		return orders;
	}

	public void setOrderUpdateBy(Long[] orderIds, Long adminUserId) {

		final Query query = em
				.createNamedQuery("SSB_UPDATE_ORDER_UPDATE_BY_BY_ORDERIDS");
		List<Long> orderIdss = Arrays.asList(orderIds);
		query.setParameter("orderIds", orderIdss);
		query.setParameter("adminUserId", adminUserId);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<Order> getOrderListByStatusAndAdminUserId(OrderStatus status,
			Long adminUserId) {

		final Query query = em
				.createNamedQuery("SSB_READ_ORDER_BY_STATUS_AND_ADMINUSERID");
		query.setParameter("adminUserId", adminUserId);
		query.setParameter("status", status.getType());
		List<Order> orders = query.getResultList();
		return orders;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Order> getAllOrdersAfterSubmitted(Long customerId) {
		final Query query = em
				.createNamedQuery("SSB_READ_ORDER_AFTER_SUBMITTED");
		query.setParameter("customerId", customerId);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Order> getOrderbyIds(Long[] orderIds) {
		final Query query = em.createNamedQuery("SSB_READ_ORDER_BY_ORDERIDS");
		List<Long> orderIdss = Arrays.asList(orderIds);
		query.setParameter("orderIds", orderIdss);
		List<Order> orders = query.getResultList();
		return orders;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Order> getAllOrdersAfterSubmittedIntime(Long customerid,
			Date beforetime, Date aftertime) {
		final Query query = em.createNamedQuery("SSB_READ_ORDER_SUBMITTED_IN");
		query.setParameter("customerId", customerid);
		query.setParameter("begintime", beforetime);
		query.setParameter("endtime", aftertime);
		List<Order> orders = query.getResultList();
		return orders;
	}
}
