package com.ssbusy.core.carbarn.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ssbusy.core.carbarn.dao.CarbarnDao;
import com.ssbusy.core.carbarn.domain.Carbarn;

/**
 * 
 * @author song
 * 
 */
@Service("carbarnService")
public class CarbarnServiceImpl implements CarbarnService {

	private static final double EARTH_RADIUS = 6378.137;
	private static final String SORT_BY_PRICE="price";

	@Resource(name = "carbarnDao")
	protected CarbarnDao carbarnDao;

	@Override
	public List<Carbarn> readCarbarnByCarbarnName(String carbarnName) {
		if ("".equals(carbarnName)) {
			return null;
		} else {
			return carbarnDao.readCarbarnByCarbarnName(carbarnName);
		}
	}

	@Override
	public List<Carbarn> readCarbarnByCarbarnAddress(String carbarnAddress) {
		if ("".equals(carbarnAddress)) {
			return null;
		} else {
			return carbarnDao.readCarbarnByCarbarnAddress(carbarnAddress);
		}
	}

	@Override
	public List<Carbarn> readCarbarnByLatitudeAndLongitude(
			final Double latitude, final Double longitude,String sortBy) {
		// TODO 这里设置经纬度的范围。
		Double radius = 0.01666667d;
		if (latitude == null || longitude == null) {
			return Collections.emptyList();
		}
		List<Carbarn> carbarns = carbarnDao.readCarbarnByLatitudeAndLongitude(
				latitude, latitude + radius, longitude, longitude + radius);
		if(SORT_BY_PRICE.equals(sortBy)){
			Collections.sort(carbarns, new Comparator<Carbarn>() {
				@Override
				public int compare(Carbarn carbarn1, Carbarn carbarn2) {
					return carbarn1.getPrice().compareTo(carbarn2.getPrice());
				}
			});
		}else{
			Collections.sort(carbarns, new Comparator<Carbarn>() {
				@Override
				public int compare(Carbarn carbarn1, Carbarn carbarn2) {
					if (carbarn1.getCartEntrances().isEmpty()
							|| carbarn2.getCartEntrances().isEmpty()) {
						return 0;
					}
					if (getDistance(latitude, longitude, carbarn1
							.getCartEntrances().get(0).getLatitude(), carbarn1
							.getCartEntrances().get(0).getLongitude()) > getDistance(
							latitude, longitude, carbarn2.getCartEntrances().get(0)
									.getLatitude(), carbarn2.getCartEntrances()
									.get(0).getLongitude())) {
						return 1;
					} else {
						return -1;
					}
				}
			});
		}
		return carbarns;
	}

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	public static double getDistance(double latitude, double longitude,
			double latitudeCompare, double longitudeCompare) {
		double radLat1 = rad(latitude);
		double radLat2 = rad(latitudeCompare);
		double a = radLat1 - radLat2;
		double b = rad(longitude) - rad(longitudeCompare);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

}
