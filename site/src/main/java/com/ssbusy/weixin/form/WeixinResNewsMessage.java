package com.ssbusy.weixin.form;

import java.util.List;

public class WeixinResNewsMessage extends WeixinResBaseMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 图文消息个数，限制为10条以内
	private int ArticleCount;
	// 多条图文消息信息，默认第一个item为大图
	private List<ResArticle> ResArticle;

	public int getArticleCount() {
		return ArticleCount;
	}

	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}

	public List<ResArticle> getResArticle() {
		return ResArticle;
	}

	public void setResArticle(List<ResArticle> resArticle) {
		ResArticle = resArticle;
	}

}
