/**
 * sudaw copy right 1.0 
 */
package com.ssbusy.core.offer.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.broadleafcommerce.common.presentation.AdminPresentationClass;
import org.broadleafcommerce.common.presentation.PopulateToOneFieldsEnum;
import org.broadleafcommerce.core.offer.domain.OfferCodeImpl;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * MyOfferCodeImpl.java
 * 
 * @author Ju
 */
@Entity
@Table(name = "SSB_OFFER_CODE")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "blOrderElements")
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.FALSE, friendlyName = "OfferCodeImpl_ssbOfferCode")
public class MyOfferCodeImpl extends OfferCodeImpl implements MyOfferCode {

	public static final long serialVersionUID = 1L;

	/** 领取券的人 */
	private Long ownerCustomerId;
	/** 用券的人 */
	private Long userCustomerId;

	private Date ownDate;

	@Override
	public Long getOwnerCustomerId() {
		return ownerCustomerId;
	}

	@Override
	public void setOwnerCustomerId(Long ownerCustomerId) {
		this.ownerCustomerId = ownerCustomerId;
	}

	@Override
	public Long getUserCustomerId() {
		return userCustomerId;
	}

	@Override
	public void setUserCustomerId(Long userCustomerId) {
		this.userCustomerId = userCustomerId;
	}

	@Override
	public int getMaxUses() {
		if (super.maxUses == null)
			return -1;
		return super.getMaxUses();
	}

	/**
	 * @return the ownDate
	 */
	public Date getOwnDate() {
		return ownDate;
	}

	/**
	 * @param ownDate the ownDate to set
	 */
	public void setOwnDate(Date ownDate) {
		this.ownDate = ownDate;
	}
}
