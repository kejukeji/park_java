package org.broadleafcommerce.core.offer.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.broadleafcommerce.common.money.Money;

@Entity
@Table(name = "SSB_ORDER_ADJUSTMENT")
public class MyOrderAdjustmentImpl extends OrderAdjustmentImpl implements
		MyOrderAdjustment {

	private static final long serialVersionUID = 1L;

	@Override
	public Money getValue() {
		// 对于返现的优惠，先不给优惠，在最后订单complete时，再将优惠追加到余额
		if (offer instanceof MyOffer
				&& Boolean.TRUE.equals(((MyOffer) offer).isAddToBalance()))
			return Money.zero(order.getCurrency().getCurrencyCode());
		return super.getValue();
	}

	/**
	 * @return 返回真实的优惠值
	 */
	@Override
	public Money getActualValue() {
		return super.getValue();
	}
}
