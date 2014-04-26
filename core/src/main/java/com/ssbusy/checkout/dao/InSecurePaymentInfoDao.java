package com.ssbusy.checkout.dao;

import org.broadleafcommerce.core.payment.domain.Referenced;

import com.ssbusy.payment.service.type.IntegrlPaymentInfo;

public interface InSecurePaymentInfoDao {
 
	  public IntegrlPaymentInfo findInInfo(String referenceNumber);
	  public IntegrlPaymentInfo  createInPaymentInfo();
	  public Referenced save(Referenced securePaymentInfo);
	  public void delete(Referenced securePaymentInfo); 
	
}
