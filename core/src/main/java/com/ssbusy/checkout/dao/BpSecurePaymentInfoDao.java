package com.ssbusy.checkout.dao;

import org.broadleafcommerce.core.payment.domain.Referenced;

import com.ssbusy.payment.service.type.BpPaymentInfo;

public interface BpSecurePaymentInfoDao {
 
	  public BpPaymentInfo findBpInfo(String referenceNumber);
	  public BpPaymentInfo createBpPaymentInfo();
	  public Referenced save(Referenced securePaymentInfo);
	  public void delete(Referenced securePaymentInfo); 
	
}
