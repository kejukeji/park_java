package com.ssbusy.core.inneraddress.service;

import java.util.List;

import com.ssbusy.core.domain.AreaAddress;
import com.ssbusy.core.domain.Dormitory;

public interface DormitoryService {
	public List<Dormitory> listDormitoriesByAreaAddress(AreaAddress areaAddress);

	public Dormitory loadDormitotyById(Long dormitoryId);
}
