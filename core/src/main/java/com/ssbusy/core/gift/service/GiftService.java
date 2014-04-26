package com.ssbusy.core.gift.service;

import com.ssbusy.core.account.domain.MyCustomer;
import com.ssbusy.core.offer.domain.MyOfferCode;

public interface GiftService {
	MyOfferCode getgift(MyCustomer customer);

	/**
	 * @param customer
	 * @param myOfferCode
	 */
	void updateOwnerCustomer(MyCustomer customer, MyOfferCode myOfferCode);
}
