package com.ssbusy.core.domain;

import org.broadleafcommerce.profile.core.domain.Address;

public interface MyAddress extends Address{

	public Dormitory getDormitory();

	public void setDormitory(Dormitory dormitory);

	public String getRoomNo();

	public void setRoomNo(String roomNo);


}
