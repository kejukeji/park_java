package com.ssbusy.controller.rechargeablecard;

import java.io.Serializable;

public class RechargeableCardForm implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String card_id;
	private String card_password;
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	public String getCard_password() {
		return card_password;
	}
	public void setCard_password(String card_password) {
		this.card_password = card_password;
	}
}
