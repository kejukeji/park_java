package com.ssbusy.checkout.dao;

import org.broadleafcommerce.core.payment.domain.Referenced;

import com.ssbusy.payment.service.type.AlipayPaymentInfo;

public interface AlipaySecurePaymentInfoDao {
	  public AlipayPaymentInfo findAlipayInfo(String referenceNumber);
	  public AlipayPaymentInfo createAlipayPaymentInfo();
	  public Referenced save(Referenced securePaymentInfo);
	  public void delete(Referenced securePaymentInfo); 
}
