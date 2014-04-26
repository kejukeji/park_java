package com.ssbusy.core.region.dao;

import java.util.List;


import com.ssbusy.core.region.domain.Region;

public interface RegionDao {
	 public List<Region> getRegion();
	 public Region getRegion(Long id);
}
