package com.ssbusy.core.catalog.domain;

import java.util.List;

import org.broadleafcommerce.core.catalog.domain.Category;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.inventory.domain.FulfillmentLocation;

public interface ProductList {

	Category getCategory();

	void setCategory(Category cat);

	FulfillmentLocation getLocation();

	void setLocation(FulfillmentLocation loc);

	List<Product> getProducts();

	void setProducts(List<Product> products);
}
