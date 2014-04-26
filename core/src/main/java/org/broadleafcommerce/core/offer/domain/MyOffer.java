package org.broadleafcommerce.core.offer.domain;

public interface MyOffer extends Offer {

	/**
	 * @return 是否在订单complete时，将该折扣部分返现到用户余额
	 */
	Boolean isAddToBalance();

	/**
	 * @param addToBalance
	 *            是否在订单complete时，将该折扣部分返现到用户余额
	 */
	void setAddToBalance(Boolean addToBalance);

}
