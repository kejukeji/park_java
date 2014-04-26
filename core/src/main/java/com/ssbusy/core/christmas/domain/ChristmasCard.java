package com.ssbusy.core.christmas.domain;

import java.io.Serializable;

public interface ChristmasCard extends Serializable {

	public Long getId();

	public void setId(Long id);

	public Long getCustomerId();

	public void setCustomerId(Long customerId);

	public int getSignDate();

	public void setSignDate(int signDate);
}
