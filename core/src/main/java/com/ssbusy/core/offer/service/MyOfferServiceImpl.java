/**
 * sudaw copy right 1.0 
 */
package com.ssbusy.core.offer.service;

import java.util.Iterator;
import java.util.List;

import org.broadleafcommerce.core.offer.domain.OfferCode;
import org.broadleafcommerce.core.offer.service.OfferServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import com.ssbusy.core.offer.dao.MyOfferCodeDao;
import com.ssbusy.core.offer.domain.MyOfferCode;

/**
 * MyOfferServiceImpl.java
 * 
 * @author Ju
 */
public class MyOfferServiceImpl extends OfferServiceImpl implements
		MyOfferService {
	@Override
	@Transactional("blTransactionManager")
	public MyOfferCode retreiveUnownedOfferCode(Long offerId,
			Long ownerCustomerId) {
		return ((MyOfferCodeDao) offerCodeDao).retreiveUnownedOfferCode(
				offerId, ownerCustomerId);
	}

	@Override
	@Transactional("blTransactionManager")
	public void updateOwnerCustomer(Long ownerCustomerId, Long offerCodeId) {
		((MyOfferCodeDao) offerCodeDao).updateOwnerCustomer(offerCodeId,
				ownerCustomerId);
	}

	@Override
	@Transactional("blTransactionManager")
	public void incrementOfferCodeUsage(OfferCode offerCode) {
		((MyOfferCodeDao) offerCodeDao).incrementOfferCodeUsage(offerCode
				.getId());
	}

	@Override
	public List<MyOfferCode> listOfferCodeByOwner(Long ownerCustomerId) {
		return ((MyOfferCodeDao) offerCodeDao)
				.listOfferCodeByOwner(ownerCustomerId);
	}

	@Override
	public long countOfferCodeByOwner(Long ownerCustomerId) {
		return ((MyOfferCodeDao) offerCodeDao)
				.countOfferCodeByOwner(ownerCustomerId);
	}

	@Override
	protected List<OfferCode> removeOutOfDateOfferCodes(
			List<OfferCode> offerCodes) {
		List<OfferCode> codes = super.removeOutOfDateOfferCodes(offerCodes);
		// 把超过用量的也移除
		for (Iterator<OfferCode> itr = codes.iterator(); itr.hasNext();) {
			OfferCode c = itr.next();
			if (c.getMaxUses() > 0 && c.getUses() >= c.getMaxUses())
				itr.remove();
		}
		return codes;
	}
}
