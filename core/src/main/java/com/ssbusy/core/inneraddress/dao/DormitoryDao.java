package com.ssbusy.core.inneraddress.dao;

import java.util.List;

import com.ssbusy.core.domain.AreaAddress;
import com.ssbusy.core.domain.Dormitory;

public interface DormitoryDao {
	public List<Dormitory> getDormitoryByArea(AreaAddress areaAddress);

	public Dormitory loadDormitoryById(Long dormitoryId);
}
