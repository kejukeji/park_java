package com.ssbusy.core.product.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.broadleafcommerce.common.currency.domain.BroadleafCurrency;
import org.broadleafcommerce.core.catalog.domain.Sku;
import org.springframework.stereotype.Service;

import com.ssbusy.core.sku.dao.MySkuDao;

@Service("ssbProductService")
public class ProductServiceImpl implements ProductService {
	@Resource(name = "ssbmySkuDao")
	protected MySkuDao mySkuDao;

	@Deprecated
	public List<Sku> findProductsByPriceAndCurrency(BigDecimal price,
			BroadleafCurrency currency) {
		if (price.intValue() != price.floatValue()) {
			return mySkuDao.readSkuByPriceAndCurrency(price, currency);
		} else {
			return Collections.emptyList();
		}
	}
}
