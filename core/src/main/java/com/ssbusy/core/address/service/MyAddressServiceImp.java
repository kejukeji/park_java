package com.ssbusy.core.address.service;

import javax.annotation.Resource;

import org.broadleafcommerce.profile.core.service.AddressServiceImpl;

import com.ssbusy.core.address.dao.MyAddressDao;
import com.ssbusy.core.domain.MyAddress;

public class MyAddressServiceImp extends AddressServiceImpl implements MyAddressService{

	@Resource(name="blAddressDao")
	protected MyAddressDao myAddressDao;

	@Override
	public MyAddress saveMyAddress(MyAddress myAddress) {

		return myAddressDao.save(myAddress);
	}

	@Override
	public MyAddress readMyAddressById(Long addressId) {
		return myAddressDao.readMyAddressById(addressId);
	}

	@Override
	public MyAddress createMyAddress() {
		return myAddressDao.createMyAddress();
	}
	
	
}
