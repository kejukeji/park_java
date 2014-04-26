package com.ssbusy.core.vote.domin;

import java.io.Serializable;

public interface Vote extends Serializable {

	public Long getCustomerId();

	public void setCustomerId(Long customerId);

	public int getTeamId();

	public void setTeamId(int teamId);

	public String getTeamName();

	public void setTeamName(String teamName);
}
