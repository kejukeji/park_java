package com.ssbusy.core.inneraddress.service;

import java.util.List;

import com.ssbusy.core.domain.AreaAddress;
import com.ssbusy.core.region.domain.Region;

public interface AreaService {
	public List<AreaAddress> listAreasByRegion(Region region);
	public AreaAddress getAreaById(Long areaId);
}
