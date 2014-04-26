package com.ssbusy.core.like.dao;

import java.util.List;
import org.broadleafcommerce.core.catalog.domain.Product;
import com.ssbusy.core.like.domain.CustomerLike;


public interface LikeDao {
	public CustomerLike load(Product product, Long customerId);

	public CustomerLike load(Long productId, Long customerId);

	public List<Product> load(Long customerId);
	public CustomerLike create();

	public void insert(CustomerLike customerLike);

	public void delete(CustomerLike customerLike);
}
