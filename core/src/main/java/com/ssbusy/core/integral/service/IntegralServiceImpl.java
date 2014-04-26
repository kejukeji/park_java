package com.ssbusy.core.integral.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssbusy.core.integral.dao.IntegralDao;
import com.ssbusy.core.integral.domain.Integral;

@Service("ssbIntegralService")
public class IntegralServiceImpl implements IntegralService{
	@Resource(name = "ssbIntegralDao")
	protected IntegralDao integralDao;
	
	@Transactional("blTransactionManager")
	public Boolean IntegralForRegister(Long customerid){
		if(integralDao.changeIntegralForRegister(customerid))
			return true;
		return false;
	}

	@Override
	@Transactional("blTransactionManager")
	public Boolean gainIntegralForPayment(Long customerId, int value) {
			integralDao.addIntegral(customerId, value);
		return true;

	}

	@Override
	@Transactional("blTransactionManager")
	public int viewCustomerIntegral(Long customerId) {
		
		return integralDao.getIntegralByCustomerId(customerId);
	}

	@Override
	public List<Integral> getIntegralHistoryList(Long customerId) {
		return integralDao.getIntegralHistoryListByCustomerId(customerId);
	}
	
	@Override
	public boolean payByIntegrl(Long customerid, int value) {
	   
		integralDao.minusIntegrl(customerid, value);
	     return true;
	}
}
