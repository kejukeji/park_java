package com.ssbusy.core.carbarn.dao;

import java.util.List;

import com.ssbusy.core.carbarn.domain.Carbarn;

public interface CarbarnDao {

	public Carbarn readCarbarnById(Long id);
	
	public List<Carbarn> readCarbarnByCarbarnName(String carbarnName);

	public List<Carbarn> readCarbarnByCarbarnAddress(String carbarnAddress);

	public List<Carbarn> readCarbarnByLatitudeAndLongitude(Double beginLatitude,Double endLatitude,Double beginLongitude,
			Double endLongitude);
	
	public Carbarn updateCarbarn(Carbarn carbarn);
}
