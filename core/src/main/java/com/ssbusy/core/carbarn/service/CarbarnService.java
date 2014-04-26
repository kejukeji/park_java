package com.ssbusy.core.carbarn.service;

import java.util.List;

import com.ssbusy.core.carbarn.domain.Carbarn;

public interface CarbarnService {

	/**
	 * 
	 * @param carbarnName 车库名字
	 * @return 根据车库名称返回车库信息
	 */
	public List<Carbarn> readCarbarnByCarbarnName(String carbarnName);

	/**
	 * 
	 * @param carbarnAddress 车库地址
	 * @return 根据车库地址返回车库信息
	 */
	public List<Carbarn> readCarbarnByCarbarnAddress(String carbarnAddress);

	/**
	 * 
	 * @param latitude 维度
	 * @param longitude 经度
	 * @return 根据经纬度 返回附近的车库信息
	 */
	public List<Carbarn> readCarbarnByLatitudeAndLongitude(Double latitude,
			Double longitude);

}
