package com.ssbusy.core.sku.dao;

import java.math.BigDecimal;
import java.util.List;

import org.broadleafcommerce.common.currency.domain.BroadleafCurrency;
import org.broadleafcommerce.core.catalog.dao.SkuDao;
import org.broadleafcommerce.core.catalog.domain.Sku;

public interface MySkuDao extends SkuDao{
	public List<Sku> readSkuByPriceAndCurrency(BigDecimal price,BroadleafCurrency currency);
}
