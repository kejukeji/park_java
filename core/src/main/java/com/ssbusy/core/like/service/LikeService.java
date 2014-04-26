package com.ssbusy.core.like.service;

import java.util.List;

import org.broadleafcommerce.core.catalog.domain.Product;

public interface LikeService {
	/**
	 * @return <code>null</code>表示未操作成功；<code>true</code>表示标记为喜欢成功；
	 *         <code>false</code>表示取消喜欢；
	 */
	public Boolean toggleLike(Long productId, Long customerId);

	/**
	 * @return <code>true</code>表示将要显示喜欢；<code>false</code>表示将要显示不喜欢
	 */
	public boolean queryLike(Product product, Long customerId);

	public List<Product> showLike(Long customerId);

	public int cancelLike(Long customerId, Long productId);
}
