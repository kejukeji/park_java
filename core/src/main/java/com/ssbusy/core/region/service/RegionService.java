package com.ssbusy.core.region.service;

import java.util.List;


import com.ssbusy.core.region.domain.Region;

public interface RegionService {
	/**
	 *  
	 * @return 返回所有Region对象
	 */
	 public List<Region> listRegions();
	 /**
	  * @return 根据id返回一个Region对象
	  */
	 public Region getRegion(Long id);
}
