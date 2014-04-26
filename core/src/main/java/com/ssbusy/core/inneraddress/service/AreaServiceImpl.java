package com.ssbusy.core.inneraddress.service;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ssbusy.core.domain.AreaAddress;
import com.ssbusy.core.inneraddress.dao.AreaDao;
import com.ssbusy.core.region.domain.Region;

@Service("ssbAreaService")
public class AreaServiceImpl implements AreaService {

	@Resource(name = "ssbAreaDao")
	protected AreaDao areaDao;

	public List<AreaAddress> listAreasByRegion(Region region) {
		if (region == null)
			return Collections.emptyList();
		return areaDao.getAreaByRegion(region);
	}

	@Override
	public AreaAddress getAreaById(Long areaId) {
		return areaDao.getAreaById(areaId);
	}

}
