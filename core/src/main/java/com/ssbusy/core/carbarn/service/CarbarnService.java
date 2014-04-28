package com.ssbusy.core.carbarn.service;

import java.util.List;

import com.ssbusy.core.carbarn.domain.Carbarn;

public interface CarbarnService {

	/**
	 * 
	 * @param carbarnName 车库名字
	 * @return 根据车库名称返回车库信息;如果名字为空则返回null
	 */
	public List<Carbarn> readCarbarnByCarbarnName(String carbarnName);

	/**
	 * 
	 * @param carbarnAddress 车库地址
	 * @return 根据车库地址返回车库信息;如果地址为空则返回null
	 */
	public List<Carbarn> readCarbarnByCarbarnAddress(String carbarnAddress);

	/**
	 * 
	 * @param latitude 纬度
	 * @param longitude 经度
	 * @param sortBy 排序规则
	 * @return 根据经纬度 返回附近的车库信息;如果经纬度为null则返回null，如果对应的车库中没有入口信息，则返回null
	 */
	public List<Carbarn> readCarbarnByLatitudeAndLongitude(Double latitude,
			Double longitude,String sortBy);

}
