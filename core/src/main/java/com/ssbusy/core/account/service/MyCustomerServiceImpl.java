package com.ssbusy.core.account.service;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.core.service.CustomerServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import com.ssbusy.core.account.dao.MyCustomerDao;
import com.ssbusy.core.account.domain.MyCustomer;
import com.ssbusy.core.integral.dao.IntegralDao;
import com.ssbusy.core.rechargeablecard.dao.TransactionDetailsDao;
import com.ssbusy.core.region.domain.Region;


public class MyCustomerServiceImpl extends CustomerServiceImpl implements
		MyCustomerService {
	
	@Resource(name = "blCustomerDao")
	protected MyCustomerDao myCustomerDao;
	
	@Resource(name= "ssbIntegralDao")
	protected IntegralDao integralDao;
	
	@Resource(name="ssbTransactionDetailsDao")
    protected TransactionDetailsDao transactionDetailsDao;
	@Override
	public MyCustomer createNewCustomer() {
		return (MyCustomer) super.createNewCustomer();
	}

	@Transactional("blTransactionManager")
	public void setRegion(Region region, Long customerId) {
		myCustomerDao.setRegion(region, customerId);
	}

	public BigDecimal getCustomerBalance(Long customerid){
		return myCustomerDao.getBalance(customerid);
	}

	@Override
	public MyCustomer getMyCustomerById(Long customerId) {
		if(myCustomerDao.getMyCustomerById(customerId)==null)
		return null;
		else return myCustomerDao.getMyCustomerById(customerId);
	}
	
	@Transactional("blTransactionManager")
	public boolean rechargeToAccountBalance(Long customerid, BigDecimal value,BalanceChangeType type) {
		if(myCustomerDao.addAccountBalance(customerid, value)) {
			if(value!=(new BigDecimal(0))){
			transactionDetailsDao.addtransactionDetails(customerid, value,type,new Date());
			}else{
				
			}
			return true;
		}
		return false;
	}

	@Override
	@Transactional("blTransactionManager")
	public boolean payFromBalance(Long customerId, BigDecimal value,BalanceChangeType type) {
		if (myCustomerDao.deductBalance(customerId, value)) {
			if(value!=(new BigDecimal(0))){
			transactionDetailsDao.addtransactionDetails(customerId,
					value.multiply(new BigDecimal(-1)), type, new Date());
			}else{
				
			}
			return true;
		}
		return false;
	}
	
	@Override
	@Transactional("blTransactionManager")
    public Customer registerCustomer(Customer customer, String password, String passwordConfirm) {
		Customer retCustomer = super.registerCustomer(customer, password, passwordConfirm);
        integralDao.changeIntegralForRegister(retCustomer.getId());
        return retCustomer;
    }
	
	public String getShaPassword(String currentPassword){
		
		return passwordEncoder.encodePassword(currentPassword, getSalt());
	}
	
}
