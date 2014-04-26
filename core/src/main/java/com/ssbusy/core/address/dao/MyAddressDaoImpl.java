package com.ssbusy.core.address.dao;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.broadleafcommerce.common.persistence.EntityConfiguration;
import org.broadleafcommerce.profile.core.dao.AddressDaoImpl;

import com.ssbusy.core.domain.MyAddress;
import com.ssbusy.core.domain.MyAddressImpl;

public class MyAddressDaoImpl extends AddressDaoImpl implements MyAddressDao {
	@PersistenceContext(unitName = "blPU")
	protected EntityManager em;

	@Resource(name = "blEntityConfiguration")
	protected EntityConfiguration entityConfiguration;

	@Override
	public MyAddress save(MyAddress myAddress) {
		return em.merge(myAddress);
	}

	@Override
	public MyAddress createMyAddress() {
		MyAddress myAddress = (MyAddress) entityConfiguration
				.createEntityInstance("org.broadleafcommerce.profile.core.domain.Address");
		myAddress.setId(null);
		return myAddress;
	}

	@Override
	public MyAddress readMyAddressById(Long id) {
		return (MyAddress) em.find(MyAddressImpl.class, id);
	}

}
