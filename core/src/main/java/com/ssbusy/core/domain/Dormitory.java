package com.ssbusy.core.domain;

import java.io.Serializable;

public interface Dormitory extends Serializable {
	
	public Long getDormitoryId();

	public void setDormitoryId(Long dormitoryId);

	public String getDormitoryName();

	public void setDormitoryName(String dormitoryName);

	public AreaAddress getAreaAddress();

	public void setAreaAddress(AreaAddress areaAddress);

}
