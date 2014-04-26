package com.ssbusy.core.domain;

import java.io.Serializable;

import com.ssbusy.core.region.domain.Region;

public interface AreaAddress extends Serializable {
	
	public Long getAreaId();

	public void setAreaId(Long areaId);

	public String getAreaName();

	public void setAreaName(String areaName);

	public Region getRegion();

	public void setRegion(Region region);

}
