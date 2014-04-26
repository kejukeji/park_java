package com.ssbusy.core.product.domain;

import org.broadleafcommerce.core.catalog.domain.Product;

public interface MyProduct extends Product {

	public Long getTotalSaled();

	public void setTotalSaled(Long totalSaled);

	public Long getTotalLike();

	public void setTotalLike(Long totalLike);

	public Long getJifen();

	public void setJifen(Long jifen);
}
