package com.ssbusy.weixin.form;

public class WeixinReqTextMessage extends WeixinReqBaseMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 消息内容
	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
}
