package com.ssbusy.core.account.domain;

import java.math.BigDecimal;

import org.broadleafcommerce.profile.core.domain.Customer;

import com.ssbusy.core.region.domain.Region;

public interface MyCustomer extends Customer {
	
	/**
	 * @return 
	 */
	public String getAvatarUrl();

	public void setAvatarUrl(String avatarUrl);

	/**
	 * @return 
	 */

	public Region getRegion();

	public void setRegion(Region region);

	/**
	 * @return 
	 */
	public BigDecimal getBalance();

	public void setBalance(BigDecimal balance);
	
	/*
	 * @return
	 */
	public int getIntegral() ;

	public void setIntegral(int integral);
	
	public String getSex();

	public void setSex(String sex) ;
	
}
