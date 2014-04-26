package com.ssbusy.core.integral.domain;

import java.util.Date;

public interface Integral {
	 
	public Long getCustomerId() ;
	
	public void setCustomerId(Long customerId) ;
	
	public String getChangeType() ;
	
	public void setChangeType(String changeType);
	
	public int getChangeQuantity();
	
	public void setChangeQuantity(int changeQuantity);
	
	public Date getChangeDate();
	
	public void setChangeDate(Date changeDate);

}
