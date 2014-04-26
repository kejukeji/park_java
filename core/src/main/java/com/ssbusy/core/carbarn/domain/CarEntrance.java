package com.ssbusy.core.carbarn.domain;

import java.io.Serializable;

public interface CarEntrance extends Serializable {


	public Long getId();
	public void setId(Long id);
	public Double getLongitude();
	public void setLongitude(Double longitude);
	public Double getLatitude();
	public void setLatitude(Double latitude);
	public Carbarn getCarbarn();
	public void setCarbarn(Carbarn carbarn);
}
