package org.broadleafcommerce.core.offer.domain;

import org.broadleafcommerce.common.money.Money;

public interface MyOrderAdjustment extends OrderAdjustment {

	/**
	 * @return 返回真实的优惠值
	 */
	Money getActualValue();
}
