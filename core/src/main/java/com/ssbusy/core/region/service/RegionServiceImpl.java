package com.ssbusy.core.region.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssbusy.core.region.dao.RegionDao;
import com.ssbusy.core.region.domain.Region;

@Service("ssbRegionService")
public class RegionServiceImpl implements RegionService {
	@Resource(name = "ssbRegionDao")
	protected RegionDao regionDao;

	@Override
	@Transactional("blTransactionManager")
	public List<Region> listRegions() {
		return regionDao.getRegion();
	}
	@Transactional("blTransactionManager")
	public Region getRegion(Long id) {
		return regionDao.getRegion(id);
	}
}
