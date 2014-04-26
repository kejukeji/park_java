package org.broadleafcommerce.core.catalog.service;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.broadleafcommerce.core.catalog.RelatedProductDTOEx;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.catalog.domain.PromotableProduct;
import org.broadleafcommerce.core.catalog.domain.RelatedProductDTO;
import org.broadleafcommerce.core.catalog.domain.RelatedProductTypeEnum;
import org.broadleafcommerce.inventory.domain.FulfillmentLocation;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.web.core.CustomerState;

import com.ssbusy.core.account.domain.MyCustomer;
import com.ssbusy.core.product.dao.MyProductDao;
import com.ssbusy.core.region.domain.Region;

public class RelatedProductsServiceImplEx extends RelatedProductsServiceImpl {
	@Resource(name = "blProductDao")
	protected MyProductDao productDao;

	/**
	 * FIXME isCumulativeResults is not used.
	 */
	@Override
	public List<? extends PromotableProduct> findRelatedProducts(
			RelatedProductDTO dto) {
		int start = 0;
		if (dto instanceof RelatedProductDTOEx) {
			start = ((RelatedProductDTOEx) dto).getStart();
		}

		List<FulfillmentLocation> locations = null;
		Customer customer = CustomerState.getCustomer();
		if (customer != null && customer instanceof MyCustomer) {
			Region region = ((MyCustomer) customer).getRegion();
			if (region != null) {
				locations = region.getFulfillmentLocations();
			}
		}
		List<? extends PromotableProduct> ret;
		if (dto.getProductId() == null
				|| RelatedProductTypeEnum.FEATURED.equals(dto.getType())) {
			if (dto.getProductId() != null) {
				Product product = lookupProduct(dto);
				if (product == null)
					return Collections.emptyList();
				dto.setCategoryId(product.getDefaultCategory().getId());
			}
			ret = productDao.findRelatedProducts(dto.getType(), locations,
					dto.getCategoryId(), start, dto.getQuantity(), true);
		} else {
			ret = productDao.findRelatedProducts(dto.getType(), locations,
					dto.getCategoryId(), start, dto.getQuantity(), false);
		}

		return ret;
	}
}
