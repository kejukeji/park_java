package com.ssbusy.core.product.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.broadleafcommerce.common.persistence.EntityConfiguration;
import org.broadleafcommerce.core.catalog.dao.ProductDaoImpl;
import org.broadleafcommerce.core.catalog.domain.MyCrossSaleProductImpl;
import org.broadleafcommerce.core.catalog.domain.MyFeaturedProductImpl;
import org.broadleafcommerce.core.catalog.domain.MyUpSaleProductImpl;
import org.broadleafcommerce.core.catalog.domain.PromotableProduct;
import org.broadleafcommerce.core.catalog.domain.RelatedProductTypeEnum;
import org.broadleafcommerce.inventory.domain.FulfillmentLocation;

import com.ssbusy.core.product.domain.MyProduct;
import com.ssbusy.core.product.domain.MyProductImpl;

public class MyProductDaoImpl extends ProductDaoImpl implements MyProductDao {

	@PersistenceContext(unitName = "blPU")
	protected EntityManager em;

	@Resource(name = "blEntityConfiguration")
	protected EntityConfiguration entityConfiguration;

	@Override
	public MyProduct getTotalSale(Long productId) {
		MyProduct myProduct = em.find(MyProductImpl.class, productId);
		return myProduct;
	}

	@Override
	public MyProduct getTotalLike(Long productId) {
		MyProduct myProduct = em.find(MyProductImpl.class, productId);
		return myProduct;
	}

	@Override
	public void setTotalSale(Long quantity, Long productId) {
		final Query query = em
				.createNamedQuery("SSB_UPDATE_TOTALSALE_MYPRODUCT_BY_PRODUCTID");
		query.setParameter("quantity", quantity);
		query.setParameter("productid", productId);
		query.executeUpdate();
	}

	@Override
	public void addTotaLike(Long quantity, Long productId) {
		final Query query = em
				.createNamedQuery("SSB_UPDATE_TOTALLIKE_MYPRODUCT_BY_PRODUCTID");
		query.setParameter("quantity", quantity);
		query.setParameter("productid", productId);
		query.executeUpdate();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<? extends PromotableProduct> findRelatedProducts(
			RelatedProductTypeEnum type, List<FulfillmentLocation> locations,
			Long parentId, int start, Integer size, boolean byCatOrProd) {
		StringBuilder sb = new StringBuilder("from ");
		Class<?> entity;
		if (RelatedProductTypeEnum.CROSS_SALE.equals(type)) {
			entity = MyCrossSaleProductImpl.class;
		} else if (RelatedProductTypeEnum.UP_SALE.equals(type)) {
			entity = MyUpSaleProductImpl.class;
		} else {
			entity = MyFeaturedProductImpl.class;
		}
		sb.append(entity.getName()).append(" e where ");
		if (byCatOrProd)
			sb.append(" e.category.id=:parentId");
		else
			sb.append(" e.product.id=:parentId");
		if (locations != null && locations.size() > 0)
			sb.append(" and e.location in (:locations)");
		sb.append(" order by e.sequence");

		final Query query = em.createQuery(sb.toString());
		query.setParameter("parentId", parentId);
		if (locations != null)
			query.setParameter("locations", locations);
		query.setFirstResult(start);
		if (size != null)
			query.setMaxResults(size);
		return query.getResultList();
	}
}
