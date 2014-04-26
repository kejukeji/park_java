package com.ssbusy.core.like.domain;

import java.io.Serializable;

import org.broadleafcommerce.core.catalog.domain.Product;
public interface CustomerLike extends Serializable {
	public Long getId();
  
	public void setId(Long id);

	public Long getCustomerId();

	public void setCustomerId(Long customerId);

	public Product getProduct();

	public void setProduct(Product product);

}
