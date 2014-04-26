package com.ssbusy.core.rechargeablecard.domain;

import java.io.Serializable;
import java.util.Date;

public interface RechargeableCard extends Serializable{
	String getCard_id();

	void setCard_id(String card_id);

	String getCard_password();

	void setCard_password(String card_password);

	Long getId();

	void setId(Long id);

	Date getRecharge_time();

	void setRecharge_time(Date recharge_time);

	float getCard_value();

	void setCard_value(float card_value);

	Date getExpireDate();

	void setExpireDate(Date expireDate);
	
	public RechargeableCardType getCardType();
	public void setCardType(RechargeableCardType cardType);
	
}
