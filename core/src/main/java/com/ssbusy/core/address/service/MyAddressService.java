package com.ssbusy.core.address.service;

import org.broadleafcommerce.profile.core.service.AddressService;

import com.ssbusy.core.domain.MyAddress;


public interface MyAddressService extends AddressService {
	
	 
	public MyAddress saveMyAddress(MyAddress myAddress);
	
	public MyAddress readMyAddressById(Long addressId);
	
	public MyAddress createMyAddress();
}
