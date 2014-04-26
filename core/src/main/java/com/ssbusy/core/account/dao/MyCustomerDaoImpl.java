package com.ssbusy.core.account.dao;


import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.broadleafcommerce.common.persistence.EntityConfiguration;
import org.broadleafcommerce.profile.core.dao.CustomerDaoImpl;

import com.ssbusy.core.account.domain.MyCustomer;
import com.ssbusy.core.account.domain.MyCustomerImpl;
import com.ssbusy.core.region.domain.Region;

public class MyCustomerDaoImpl extends CustomerDaoImpl implements MyCustomerDao {

	@PersistenceContext(unitName = "blPU")
	protected EntityManager em;


	@Resource(name = "blEntityConfiguration")
	protected EntityConfiguration entityConfiguration;

	@Override
	public MyCustomer create() {
		MyCustomer c = (MyCustomer) super.create();
		c.setUsername(null);
		return c;
	}

	public void setRegion(Region region, Long customerId) {

		MyCustomer myCustomer = (MyCustomer) em.find(MyCustomerImpl.class,
				customerId);
		if (myCustomer != null) {
			myCustomer.setRegion(region);
			em.merge(myCustomer);
		} else {
			MyCustomer customer = createMyCustomer();
			customer.setId(customerId);
			customer.setRegion(region);
			em.merge(customer);
		}
	}

	public BigDecimal getBalance(Long customerid) {
		MyCustomer myCustomer = em.find(MyCustomerImpl.class, customerid);
		if(myCustomer!=null){		
			if(myCustomer.getBalance()!=null)
				return (BigDecimal)myCustomer.getBalance();
			return BigDecimal.ZERO; 
		}else{
			return BigDecimal.ZERO;
		}
	}

	@Override
	public MyCustomer getMyCustomerById(Long customerId) {
		return (MyCustomer) em.find(MyCustomerImpl.class, customerId);
	}

	@Override
	public MyCustomer createMyCustomer() {
		MyCustomer o = (MyCustomer) entityConfiguration
				.createEntityInstance("org.broadleafcommerce.profile.core.domain.Customer");
		o.setId(null);
		return o;
	}
	public boolean addAccountBalance(Long customerid, BigDecimal values) {

			final Query query = em.createNamedQuery("UPDATE_MYCUSTOMER_ADD_BALANCE_BY_CUSTOMERID");
			query.setParameter("value", values);
			query.setParameter("customerid", customerid);
			
			return query.executeUpdate()>0;
	}

	@Override
	public boolean deductBalance(Long customerId, BigDecimal value) {
		final Query query = em
				.createNamedQuery("UPDATE_MYCUSTOMER_DEDUCT_BALANCE_BY_CUSTOMERID");
		query.setParameter("value", value);
		query.setParameter("customerid", customerId);
		
		return query.executeUpdate()>0?true:false;
	}	
	
}
