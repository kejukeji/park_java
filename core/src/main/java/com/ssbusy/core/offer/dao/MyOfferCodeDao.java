/**
 * sudaw copy right 1.0 
 */
package com.ssbusy.core.offer.dao;

import java.util.List;

import org.broadleafcommerce.core.offer.dao.OfferCodeDao;

import com.ssbusy.core.offer.domain.MyOfferCode;

/**
 * MyOfferCodeDao.java
 * 
 * @author Ju
 */
public interface MyOfferCodeDao extends OfferCodeDao {

    /**
     * @param offerId
     * @param ownerCustomerId
     * @return
     */
    MyOfferCode retreiveUnownedOfferCode(Long offerId, Long ownerCustomerId);

	/**
	 * @param offerCodeId
	 * @param ownerCustomerId
	 */
	void updateOwnerCustomer(Long offerCodeId, Long ownerCustomerId);

	/**
	 * @param ownerCustomerId
	 * @return
	 */
	List<MyOfferCode> listOfferCodeByOwner(Long ownerCustomerId);

	/**
	 * @param id
	 */
	void incrementOfferCodeUsage(Long id);

	/**
	 * @param ownerCustomerId
	 * @return
	 */
	long countOfferCodeByOwner(Long ownerCustomerId);

}
