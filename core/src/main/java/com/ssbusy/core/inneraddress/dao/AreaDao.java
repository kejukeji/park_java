package com.ssbusy.core.inneraddress.dao;

import java.util.List;

import com.ssbusy.core.domain.AreaAddress;
import com.ssbusy.core.region.domain.Region;

public interface AreaDao {
	public List<AreaAddress> getAreaByRegion(Region region);

	public AreaAddress getAreaById(Long areaId);
}
