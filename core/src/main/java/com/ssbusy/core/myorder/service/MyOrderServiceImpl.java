package com.ssbusy.core.myorder.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.core.catalog.domain.Sku;
import org.broadleafcommerce.core.order.domain.LocationedItem;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.domain.OrderItem;
import org.broadleafcommerce.core.order.domain.SkuAccessor;
import org.broadleafcommerce.core.order.service.OrderServiceImpl;
import org.broadleafcommerce.core.order.service.call.OrderItemRequestDTO;
import org.broadleafcommerce.core.order.service.exception.AddToCartException;
import org.broadleafcommerce.core.order.service.exception.RemoveFromCartException;
import org.broadleafcommerce.core.order.service.exception.UpdateCartException;
import org.broadleafcommerce.core.order.service.type.OrderStatus;
import org.broadleafcommerce.core.payment.domain.PaymentInfo;
import org.broadleafcommerce.core.pricing.service.exception.PricingException;
import org.broadleafcommerce.inventory.domain.FulfillmentLocation;
import org.broadleafcommerce.inventory.exception.ConcurrentInventoryModificationException;
import org.broadleafcommerce.inventory.service.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssbusy.core.account.domain.MyCustomer;
import com.ssbusy.core.account.service.BalanceChangeType;
import com.ssbusy.core.account.service.MyCustomerService;
import com.ssbusy.core.myorder.dao.MyOrderDao;
import com.ssbusy.core.myorder.domain.MyOrder;
import com.ssbusy.payment.service.type.MyPaymentInfoType;

@Service("ssbMyOrderService")
public class MyOrderServiceImpl extends OrderServiceImpl implements
		MyOrderService {
	private static final Log LOG = LogFactory.getLog(MyOrderServiceImpl.class);
	@Resource(name = "ssbMyOrderDao")
	protected MyOrderDao myOrderDao;

	@Resource(name = "blInventoryService")
	protected InventoryService inventoryService;

	@Resource(name = "blCustomerService")
	protected MyCustomerService myCustomerService;

	@Override
	public List<Order> findOrdersForProcess() {

		return myOrderDao.findOrderForProcess();
	}

	@Transactional("blTransactionManager")
	public void processOrderStatus(Long[] orderIds, OrderStatus status) {

		myOrderDao.setOrderStatus(orderIds, status);

	}

	@Override
	public Long countOrderByTime(Date lastTime,
			FulfillmentLocation fulfillmentLocation) {

		return myOrderDao.countOrderByTime(lastTime, fulfillmentLocation);
	}

	@Override
	public Order loadOrderByOrderId(long orderId) {
		return myOrderDao.loadOrderByOrderId(orderId);
	}

	@Override
	public List<Order> getOrderListByOrderIdsAndAdminUserId(Long[] orderIds,
			Long adminUserId) {
		return myOrderDao.getOrderListByOrderIds(orderIds, adminUserId);

	}

	@Override
	public List<MyOrder> getOrderListByStatus(OrderStatus status,
			Long fulfillmentLocationId) {
		return myOrderDao.getOrderListByStatus(status, fulfillmentLocationId);
	}

	@Transactional("blTransactionManager")
	public void setOrderUpdateBy(Long[] orderIds, Long adminUserId) {
		myOrderDao.setOrderUpdateBy(orderIds, adminUserId);
	}

	public List<Order> getOrderListByStatusAndAdminUserId(OrderStatus status,
			Long adminUserId) {
		return myOrderDao.getOrderListByStatusAndAdminUserId(status,
				adminUserId);
	}

	@Override
	public Boolean cancelMyOrder(Order order, MyCustomer myCustomer)
			throws PricingException, ConcurrentInventoryModificationException {
		if (order == null || myCustomer == null
				|| order.getPaymentInfos() == null
				|| order.getPaymentInfos().isEmpty()
				|| order.getPaymentInfos().get(0) == null
				|| OrderStatus.NAMED.equals(order.getStatus())
				|| OrderStatus.IN_PROCESS.equals(order.getStatus())
				|| OrderStatus.CANCELLED.equals(order.getStatus())) {
			return null;
		}

		List<OrderItem> orderItems = order.getOrderItems();
		for (int i = 0; i < orderItems.size(); i++) {
			OrderItem item = orderItems.get(i);
			Sku sku = null;
			if (item instanceof SkuAccessor) {
				sku = ((SkuAccessor) item).getSku();
			}
			FulfillmentLocation location = null;
			if (item instanceof LocationedItem) {
				location = ((LocationedItem) item).getLocation();
			}
			Map<Sku, Integer> skuInventoryMap = new HashMap<Sku, Integer>(1);
			skuInventoryMap.put(sku, item.getQuantity());
			if (location == null) {
				inventoryService.incrementInventory(skuInventoryMap);
				inventoryService.incrementInventoryOnHand(skuInventoryMap);
			} else {
				inventoryService.incrementInventory(skuInventoryMap, location);
				inventoryService.incrementInventoryOnHand(skuInventoryMap,
						location);
			}
		}
		order.setStatus(OrderStatus.CANCELLED);
		super.save(order, true);
		// 退钱
		if (!"CNY".equals(order.getCurrency().getCurrencyCode())) {
			return Boolean.TRUE;
		}

		for (PaymentInfo info : order.getPaymentInfos()) {
			if (!MyPaymentInfoType.Payment_Cod.equals(info.getType())) {
				myCustomerService.rechargeToAccountBalance(myCustomer.getId(),
						info.getAmount().getAmount(), BalanceChangeType.BPBACK);
			}
		}
		return Boolean.TRUE;
	}

	@Override
	public List<Order> getAllOrdersAfterSubmitted(Long customerId) {
		return myOrderDao.getAllOrdersAfterSubmitted(customerId);
	}

	public List<Order> getOrderbyIds(Long[] orderIds) {
		return myOrderDao.getOrderbyIds(orderIds);
	}

	@SuppressWarnings("deprecation")
	@Transactional("blTransactionManager")
	@Override
	public Boolean cancelOrderItem(OrderItem orderItem, MyCustomer myCustomer,
			long quantity) throws ConcurrentInventoryModificationException {
		// quantity要大于等于0小于原订单单项的数量，不是人民币的不能退
		if (orderItem == null
				|| orderItem.getOrder() == null
				|| myCustomer == null
				|| quantity < 0
				|| OrderStatus.NAMED.equals(orderItem.getOrder().getStatus())
				|| OrderStatus.IN_PROCESS.equals(orderItem.getOrder()
						.getStatus())
				|| OrderStatus.CANCELLED.equals(orderItem.getOrder()
						.getStatus())) {
			return null;
		}
		Order order = orderItem.getOrder();
		FulfillmentLocation location = null;
		if (orderItem instanceof LocationedItem)
			location = ((LocationedItem) orderItem).getLocation();
		Sku sku = null;
		if (orderItem instanceof SkuAccessor) {
			SkuAccessor item = (SkuAccessor) orderItem;
			sku = item.getSku();
		} else
			return null;
		double orderTotal = order.getTotal().doubleValue();
		int oldQuantity = orderItem.getQuantity();
		if (quantity == 0) {
			if (LOG.isInfoEnabled()) {
				LOG.info("该Orderitem的商品实际购买数量为零，因此该orderItem会被删除，该OrderItem的信息如下：所属的orderId为："
						+ orderItem.getOrder().getId()
						+ ";skuid="
						+ sku.getId()
						+ "；该OrderItem的原先数量是："
						+ orderItem.getQuantity()
						+ "；该OrderItem更改后数量为0；该OrderItem取消前的总价为："
						+ orderItem.getTotalPrice()
						+ ";该OrderItem的优惠为："
						+ orderItem.getAdjustmentValue());
			}
		}
		OrderItemRequestDTO orderItemRequestDTO = new OrderItemRequestDTO();
		orderItemRequestDTO.setOrderItemId(orderItem.getId());
		orderItemRequestDTO.setSkuId(sku.getId());
		orderItemRequestDTO.getItemAttributes().put(
				LocationedItem.LOCATIONED_ITEM_LOCATION_ID,
				"" + (location == null ? -1 : location.getId()));
		orderItemRequestDTO.setQuantity((int) quantity);
		try {
			order = super.updateItemQuantity(order.getId(),
					orderItemRequestDTO, true);
		} catch (UpdateCartException e1) {
			LOG.error("UpdateCartException,更改数量数显异常", e1);
			return Boolean.FALSE;
		} catch (RemoveFromCartException e1) {
			LOG.error("RemoveFromCartException,更改数量数显异常", e1);
			return Boolean.FALSE;
		}

		// 退库存
		Map<Sku, Integer> skuInventoryMap = new HashMap<Sku, Integer>(1);
		skuInventoryMap.put(sku, (int) (oldQuantity - quantity));

		if (location == null) {
			inventoryService.incrementInventory(skuInventoryMap);
			inventoryService.incrementInventoryOnHand(skuInventoryMap);
		} else {
			inventoryService.incrementInventory(skuInventoryMap, location);
			inventoryService
					.incrementInventoryOnHand(skuInventoryMap, location);
		}

		// 退钱
		if (!"CNY".equals(order.getCurrency().getCurrencyCode())) {
			return Boolean.TRUE;
		}
		double delta = orderTotal - order.getTotal().doubleValue();

		for (PaymentInfo info : order.getPaymentInfos()) {
			if (MyPaymentInfoType.Payment_Cod.equals(info.getType())) {
				// 后付款
				delta -= info.getAmount().doubleValue();
				if (delta >= 0)
					info.setAmount(new Money(BigDecimal.ZERO, order
							.getCurrency()));
				else {
					info.setAmount(new Money(new BigDecimal(-delta), order
							.getCurrency()));
					break;
				}
			} else { // 预付款,需要返余额
				if (!"CNY".equals(info.getCurrency().getCurrencyCode()))
					continue;
				double reBalance = delta;
				delta -= info.getAmount().doubleValue();
				if (delta >= 0) {
					info.setAmount(new Money(BigDecimal.ZERO, order
							.getCurrency()));
					reBalance = info.getAmount().doubleValue();
				} else {
					info.setAmount(new Money(new BigDecimal(-delta), order
							.getCurrency()));
					break;
				}
				// TODO 取消订单操作的type未定
				myCustomerService
						.rechargeToAccountBalance(myCustomer.getId(),
								BigDecimal.valueOf(reBalance),
								BalanceChangeType.BPBACK);
			}

		}
		return Boolean.TRUE;
	}

	@Override
	public List<Order> getAllSubmittedInTime(Long customerID, Date begintime,
			Date endtime) {
		return myOrderDao.getAllOrdersAfterSubmittedIntime(customerID,
				begintime, endtime);
	}

	@Override
	public Order addItemFromNamedOrder(Order namedOrder, OrderItem item,
			int quantity, boolean priceOrder) throws RemoveFromCartException,
			AddToCartException, UpdateCartException {
		// Validate that the quantity requested makes sense
		if (quantity < 1 || quantity > item.getQuantity()) {
			throw new IllegalArgumentException("Cannot move 0 or less quantity");
		} else if (quantity == item.getQuantity()) {
			return addItemFromNamedOrder(namedOrder, item, priceOrder);
		}

		Order cartOrder = orderDao
				.readCartForCustomer(namedOrder.getCustomer());
		if (cartOrder == null) {
			cartOrder = createNewCartForCustomer(namedOrder.getCustomer());
		}

		if (moveNamedOrderItems) {
			// Update the old item to its new quantity only if we're moving
			// items
			OrderItemRequestDTO orderItemRequestDTO = new OrderItemRequestDTO();
			if (item instanceof LocationedItem) {
				orderItemRequestDTO.getItemAttributes().put(
						LocationedItem.LOCATIONED_ITEM_LOCATION_ID,
						"" + ((LocationedItem) item).getLocation().getId());
			}
			orderItemRequestDTO.setOrderItemId(item.getId());
			orderItemRequestDTO.setQuantity(item.getQuantity() - quantity);
			updateItemQuantity(namedOrder.getId(), orderItemRequestDTO, false);
		}

		OrderItemRequestDTO orderItemRequest = orderItemService
				.buildOrderItemRequestDTOFromOrderItem(item);
		orderItemRequest.setQuantity(quantity);
		cartOrder = addItem(cartOrder.getId(), orderItemRequest, priceOrder);

		return cartOrder;
	}
}
