package com.ssbusy.core.integral.service;

import java.util.List;

import com.ssbusy.core.integral.domain.Integral;

public interface IntegralService { 
	
	public Boolean IntegralForRegister(Long customerid);
	
	public Boolean gainIntegralForPayment(Long customerId,int value);
	
	public int viewCustomerIntegral(Long customerId);
	
	public boolean payByIntegrl(Long customerid,int value);

	public List<Integral> getIntegralHistoryList(Long customerId);

}
