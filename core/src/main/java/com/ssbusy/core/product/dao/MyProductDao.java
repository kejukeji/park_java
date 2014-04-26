package com.ssbusy.core.product.dao;

import java.util.List;

import org.broadleafcommerce.core.catalog.dao.ProductDao;
import org.broadleafcommerce.core.catalog.domain.PromotableProduct;
import org.broadleafcommerce.core.catalog.domain.RelatedProductTypeEnum;
import org.broadleafcommerce.inventory.domain.FulfillmentLocation;

import com.ssbusy.core.product.domain.MyProduct;

public interface MyProductDao extends ProductDao {

	MyProduct getTotalSale(Long productId);

	void setTotalSale(Long quantity, Long productId);

	MyProduct getTotalLike(Long productId);

	void addTotaLike(Long quantity, Long productId);

	/**
	 * @param locations
	 *            所在分仓
	 * @param byCatOrProd
	 *            true: parent is category, false: parent is product
	 * @return
	 */
	List<? extends PromotableProduct> findRelatedProducts(
			RelatedProductTypeEnum type, List<FulfillmentLocation> locations,
			Long parentId, int start, Integer size, boolean byCatOrProd);

}
