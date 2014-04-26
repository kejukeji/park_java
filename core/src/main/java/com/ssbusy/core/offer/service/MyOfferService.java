/**
 * sudaw copy right 1.0 
 */
package com.ssbusy.core.offer.service;

import java.util.List;

import org.broadleafcommerce.core.offer.domain.OfferCode;
import org.broadleafcommerce.core.offer.service.OfferService;

import com.ssbusy.core.offer.domain.MyOfferCode;

/**
 * MyOfferService.java
 * 
 * @author Ju
 */
public interface MyOfferService extends OfferService {

	/**
	 * 返回null表示获取失败；否则会将该优惠券标记为已领取状态，故重复调用返回值不同
	 * 
	 * @param offerId
	 * @param ownerCustomerId
	 * @return may null; or the offerCode entity: id always &gt; 0
	 */
	MyOfferCode retreiveUnownedOfferCode(Long offerId, Long ownerCustomerId);

	/**
	 * 直接更改offerCode的owner
	 * 
	 * @param ownerCustomerId
	 * @param offerCodeId
	 */
	void updateOwnerCustomer(Long ownerCustomerId, Long offerCodeId);

	/**
	 * 列出该客户所有没过endDate、没超用量的offer codes，不管是否还能用。
	 * @param ownerCustomerId
	 * @return
	 */
	List<MyOfferCode> listOfferCodeByOwner(Long ownerCustomerId);

	/**
	 * @param id
	 * @return
	 */
	long countOfferCodeByOwner(Long ownerCustomerId);

	/**
	 * @param offerCode
	 */
	void incrementOfferCodeUsage(OfferCode offerCode);
}
