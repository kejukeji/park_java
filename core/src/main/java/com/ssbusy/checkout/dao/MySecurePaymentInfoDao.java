package com.ssbusy.checkout.dao;

import org.broadleafcommerce.core.payment.domain.Referenced;

import com.ssbusy.payment.service.type.CodPaymentInfo;

public interface MySecurePaymentInfoDao {
     
	 
	public CodPaymentInfo findCodInfo(String referenceNumber);
	 public  CodPaymentInfo createCodPaymentInfo();
	  public Referenced save(Referenced securePaymentInfo);
	  public void delete(Referenced securePaymentInfo); 

}
