package com.ssbusy.payment.service.type;

import org.broadleafcommerce.core.payment.domain.Referenced;

public interface  CodPaymentInfo extends Referenced {
   
	public String getMessage();

	public void setMessage(String message);
	
    
}
