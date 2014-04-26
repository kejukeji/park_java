/**
 * sudaw copy right 1.0 
 */
package com.ssbusy.core.offer.dao;

import java.util.List;

import javax.persistence.Query;

import org.broadleafcommerce.core.offer.dao.OfferCodeDaoImpl;

import com.ssbusy.core.offer.domain.MyOfferCode;

/**
 * MyOfferCodeDaoImpl.java
 * 
 * @author Ju
 */
public class MyOfferCodeDaoImpl extends OfferCodeDaoImpl implements
		MyOfferCodeDao {

	@Override
	public MyOfferCode retreiveUnownedOfferCode(Long offerId,
			Long ownerCustomerId) {
		Query query = em.createNamedQuery("FIND_UNOWNED_OFFER_CODE");
		query.setParameter("offerId", offerId);
		query.setMaxResults(1);
		@SuppressWarnings("unchecked")
		List<MyOfferCode> l = query.getResultList();
		if (l.isEmpty())
			return null;
		MyOfferCode oc = l.get(0);

		query = em.createNamedQuery("UPDATE_OWN_OFFER_CODE");
		query.setParameter("id", oc.getId());
		query.setParameter("ownerCustomerId", ownerCustomerId);
		if (query.executeUpdate() > 0)
			return oc;
		return null;
	}

	@Override
	public void updateOwnerCustomer(Long offerCodeId, Long ownerCustomerId) {
		Query query = em.createNamedQuery("UPDATE_OFFER_CODE_OWNNER_CUSTOMER");
		query.setParameter("id", offerCodeId);
		query.setParameter("ownerCustomerId", ownerCustomerId);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MyOfferCode> listOfferCodeByOwner(Long ownerCustomerId) {
		Query query = em.createNamedQuery("LIST_OFFER_CODE_BY_OWNER");
		query.setParameter("ownerCustomerId", ownerCustomerId);
		return query.getResultList();
	}

	@Override
	public long countOfferCodeByOwner(Long ownerCustomerId) {
		Query query = em.createNamedQuery("COUNT_OFFER_CODE_BY_OWNER");
		query.setParameter("ownerCustomerId", ownerCustomerId);
		return (Long) query.getSingleResult();
	}

	@Override
	public void incrementOfferCodeUsage(Long id) {
		Query query = em.createNamedQuery("CHANGE_OFFER_CODE_USAGE");
		query.setParameter("id", id);
		query.setParameter("val", 1);
		query.executeUpdate();
	}

}
