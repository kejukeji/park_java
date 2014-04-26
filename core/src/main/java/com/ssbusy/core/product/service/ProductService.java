package com.ssbusy.core.product.service;

import java.math.BigDecimal;
import java.util.List;

import org.broadleafcommerce.common.currency.domain.BroadleafCurrency;
import org.broadleafcommerce.core.catalog.domain.Sku;
import org.broadleafcommerce.inventory.service.InventoryService;

public interface ProductService {
	/**
	 * @param price
	 * @param currency
	 * @return
	 * @deprecated use {@link InventoryService#findProductsByPriceAndCurrency}
	 */
	@Deprecated
	public List<Sku> findProductsByPriceAndCurrency(BigDecimal price,
			BroadleafCurrency currency);
}
