package com.ssbusy.core.gift.service;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ssbusy.core.account.domain.MyCustomer;
import com.ssbusy.core.offer.domain.MyOfferCode;
import com.ssbusy.core.offer.service.MyOfferService;

@Service("blGiftService")
public class GiftServiceImpl implements GiftService, InitializingBean {
	@Resource(name = "blOfferService")
	private MyOfferService myofferService;
	@Value("${offercode.offer.id}")
	private long[] offerids;
	@Value("${offercode.chance}")
	private double[] offercode_chances;

	private double[] offerCodeRange;

	@Override
	public void afterPropertiesSet() throws Exception {
		if (offerids == null || offercode_chances == null
				|| offerids.length == 0 || offercode_chances.length == 0)
			return;
		int len = Math.min(offercode_chances.length, offerids.length);

		offerCodeRange = new double[len];
		double cululatedP = 0;
		for (int i = 0; i < len; i++) {
			cululatedP += offercode_chances[i];
			offerCodeRange[i] = cululatedP;
		}
	}

	@Override
	public MyOfferCode getgift(MyCustomer customer) {
		if (offerCodeRange == null)
			return null;
		MyOfferCode myOfferCode = null;
		double random = Math.random();

		for (int i = 0; i < offerCodeRange.length; i++) {
			if (random < offerCodeRange[i]) {
				// 看看用户已中多少, 已中得越多，获奖几率越小
				if (customer.isRegistered()) {
					long ownCnt = myofferService.countOfferCodeByOwner(customer
							.getId());
					ownCnt++;
					random *= ownCnt;
					i = -1;
					for (int j = 0; j < offerCodeRange.length; j++) {
						if (random < offerCodeRange[j]) {
							i = j;
							break;
						}
					}
				}
				if (i >= 0)
					myOfferCode = myofferService.retreiveUnownedOfferCode(
							offerids[i], customer.getId());
				break;
			}
		}
		return myOfferCode;
	}

	@Override
	public void updateOwnerCustomer(MyCustomer customer, MyOfferCode myOfferCode) {
		myOfferCode.setOwnerCustomerId(customer.getId());
		myOfferCode.setOwnDate(new Date());
		myofferService.updateOwnerCustomer(customer.getId(),
				myOfferCode.getId());
	}

}
