package com.ssbusy.weixin.form;

public class WeixinResTextMessage extends WeixinResBaseMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 回复的消息内容
	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
}
