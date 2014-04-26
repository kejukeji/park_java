package com.ssbusy.core.product.myservice;

import com.ssbusy.core.product.domain.MyProduct;

public interface MyProductService{
	/**
	 * 
	 * @param productId
	 * @return <code>warn:</code>返回值可能为0，null
	 */
	MyProduct getTotalSale(Long productId);
	
	void setTotalSale(Long quantity,Long productId);
	
	/**
	 * 
	 * @param productId
	 * @return <code>warn:</code>返回值可能为0，null
	 */
	MyProduct getTotalLike(Long productId);
	
	void addTotalLike(Long quantity,Long productId);

}
