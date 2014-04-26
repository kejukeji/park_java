/**
 * sudaw copy right 1.0 
 */
package com.ssbusy.core.offer.domain;

import java.util.Date;

import org.broadleafcommerce.core.offer.domain.OfferCode;

/**
 * 
 * 
 * MyOfferCode.java
 * 
 * @author Ju
 */
public interface MyOfferCode extends OfferCode {

    /** 领取券的人  */
    Long getOwnerCustomerId();

    void setOwnerCustomerId(Long ownerCustomerId);
	Date getOwnDate();

	void setOwnDate(Date ownDate);

    Long getUserCustomerId();

    /** 用券的人  */
    void setUserCustomerId(Long userCustomerId);
}
