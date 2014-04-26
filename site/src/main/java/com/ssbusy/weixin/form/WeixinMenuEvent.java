package com.ssbusy.weixin.form;

public class WeixinMenuEvent extends WeixinBaseEvent {

	public String getEventKey() {
		return EventKey;
	}

	public void setEventKey(String eventKey) {
		EventKey = eventKey;
	}

	private String EventKey;

}