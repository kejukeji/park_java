package com.ssbusy.core.integral.dao;

import java.util.List;

import com.ssbusy.core.integral.domain.Integral;

public interface IntegralDao {
	
	public Boolean changeIntegralForRegister(Long customerid);
	 
	public int getIntegralByCustomerId(Long customerid);
	
	public void addIntegral(Long customerId,int value);
	public void minusIntegrl(Long customerid,int value);
	public List<Integral> getIntegralHistoryListByCustomerId(Long customerId);
}
