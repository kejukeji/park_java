package com.ssbusy.core.address.dao;

import org.broadleafcommerce.profile.core.dao.AddressDao;

import com.ssbusy.core.domain.MyAddress;

public interface MyAddressDao extends AddressDao {

	public MyAddress save(MyAddress myAddress);

	public MyAddress createMyAddress();

	public MyAddress readMyAddressById(Long id);
}
