package com.ssbusy.core.account.service;

import java.math.BigDecimal;

import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.core.service.CustomerService;

import com.ssbusy.core.account.domain.MyCustomer;
import com.ssbusy.core.region.domain.Region;

public interface MyCustomerService extends CustomerService {

	MyCustomer createNewCustomer();

	public void setRegion(Region region, Long CustomerId);

	public BigDecimal getCustomerBalance(Long customerid);

	public boolean rechargeToAccountBalance(Long customerid, BigDecimal value,BalanceChangeType type);

	MyCustomer getMyCustomerById(Long customerId);

	public boolean payFromBalance(Long customerId, BigDecimal orderFee,BalanceChangeType type);

	public Customer saveCustomer(Customer customer, boolean register);
	
	public String getShaPassword(String currentPassword);

}
