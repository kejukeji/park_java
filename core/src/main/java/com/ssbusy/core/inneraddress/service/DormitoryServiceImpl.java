package com.ssbusy.core.inneraddress.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ssbusy.core.domain.AreaAddress;
import com.ssbusy.core.domain.Dormitory;
import com.ssbusy.core.inneraddress.dao.DormitoryDao;

@Service("ssbDormitoryService")
public class DormitoryServiceImpl implements DormitoryService{
	@Resource(name = "ssbDormitoryDao")
	protected DormitoryDao dormitoryDao;
	public List<Dormitory> listDormitoriesByAreaAddress(AreaAddress areaAddress) {
		return dormitoryDao.getDormitoryByArea(areaAddress);
	}
	@Override
	public Dormitory loadDormitotyById(Long dormitoryId) {
		return dormitoryDao.loadDormitoryById(dormitoryId);
	}

}
