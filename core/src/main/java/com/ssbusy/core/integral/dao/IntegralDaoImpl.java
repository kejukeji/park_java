package com.ssbusy.core.integral.dao;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.ssbusy.core.account.dao.MyCustomerDao;
import com.ssbusy.core.account.domain.MyCustomer;
import com.ssbusy.core.account.domain.MyCustomerImpl;
import com.ssbusy.core.integral.domain.Integral;
import com.ssbusy.core.integral.domain.IntegralImpl;

@Repository("ssbIntegralDao") 
public class IntegralDaoImpl implements IntegralDao {
	
	@Resource(name = "blCustomerDao")
	protected MyCustomerDao myCustomerDao;
	
	@PersistenceContext(unitName = "blPU")
	protected EntityManager em;
	
	private static int integralForRegister = 20;
	private static String registerType ="注册得仙丹";
	private static String consumeType = "仙丹支付";
	private static String consumeGainType="消费得仙丹";
	
	@Override
	public Boolean changeIntegralForRegister(Long customerid) {
		
		MyCustomer myCustomer = em.find(MyCustomerImpl.class, customerid);
		if(myCustomer==null){
			return false;
		}
		Integral integral = new IntegralImpl();
		integral.setCustomerId(customerid);
		integral.setChangeType(registerType);
		integral.setChangeQuantity(integralForRegister);
		integral.setChangeDate(new Date());
		em.merge(integral);
		myCustomer.setIntegral(myCustomer.getIntegral()+integralForRegister);
		em.merge(myCustomer);
		return true;
	}

	@Override
	public int getIntegralByCustomerId(Long customerid) {
		
		MyCustomer myCustomer = em.find(MyCustomerImpl.class, customerid);
		if(myCustomer==null)
			return 0;
		return myCustomer.getIntegral();
	}

	@Override
	public void addIntegral(Long customerId, int value) {
		MyCustomer myCustomer = em.find(MyCustomerImpl.class, customerId);
		myCustomer.setIntegral(myCustomer.getIntegral()+value);
		em.merge(myCustomer);
		Integral integral = new IntegralImpl();
		integral.setCustomerId(customerId);
		integral.setChangeType(consumeGainType);
		integral.setChangeQuantity(value);
		integral.setChangeDate(new Date());
		em.merge(integral);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integral> getIntegralHistoryListByCustomerId(Long customerId) {
		String queryString = "FROM com.ssbusy.core.integral.domain.Integral integrals where integrals.customerId=:customerId";
		final Query query = em.createQuery(queryString);
		query.setParameter("customerId", customerId);
		return query.getResultList();
	}
	@Override
	public void minusIntegrl(Long customerId, int value) {
		MyCustomer myCustomer = em.find(MyCustomerImpl.class, customerId);
		myCustomer.setIntegral(myCustomer.getIntegral()-value);
		em.merge(myCustomer);
		Integral integral = new IntegralImpl();
		integral.setCustomerId(customerId);
		integral.setChangeType(consumeType);
		integral.setChangeQuantity(value);
		integral.setChangeDate(new Date());
		em.merge(integral);
		
	}
}
