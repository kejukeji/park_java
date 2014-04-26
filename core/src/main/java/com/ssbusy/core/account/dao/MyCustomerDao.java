package com.ssbusy.core.account.dao;

import java.math.BigDecimal;

import org.broadleafcommerce.profile.core.dao.CustomerDao;

import com.ssbusy.core.account.domain.MyCustomer;
import com.ssbusy.core.region.domain.Region;

public interface MyCustomerDao extends CustomerDao {

	public void setRegion(Region region,Long customerId);

	public BigDecimal getBalance(Long customerid);
	
	public boolean addAccountBalance(Long customerid,BigDecimal value);

	public MyCustomer getMyCustomerById(Long customerId);
	
	public MyCustomer createMyCustomer();
	
	public boolean deductBalance(Long customerId,BigDecimal value);
	
	
}
