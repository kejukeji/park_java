package com.ssbusy.core.sku.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.broadleafcommerce.common.currency.domain.BroadleafCurrency;
import org.broadleafcommerce.common.persistence.EntityConfiguration;
import org.broadleafcommerce.core.catalog.dao.SkuDaoImpl;
import org.broadleafcommerce.core.catalog.domain.Sku;
import org.springframework.stereotype.Repository;

@Repository("ssbmySkuDao")
public class MySkuDaoImpl extends SkuDaoImpl implements MySkuDao {
	@PersistenceContext(unitName = "blPU")
	protected EntityManager em;
	@Resource(name = "blEntityConfiguration")
	protected EntityConfiguration entityConfiguration;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Sku> readSkuByPriceAndCurrency(BigDecimal price,BroadleafCurrency currency) {
		    Query query = em.createNamedQuery("BC_READ_SKUS_BY_PRICE");
	        query.setParameter("price", price);
	        query.setParameter("currency", currency);
	      //  query.setMaxResults(couzheng_show_quantity);
	        return  query.getResultList();
		//return null;
	}

}
