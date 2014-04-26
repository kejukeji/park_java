package com.ssbusy.core.like.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.broadleafcommerce.common.persistence.EntityConfiguration;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.springframework.stereotype.Repository;

import com.ssbusy.core.like.domain.CustomerLike;

@Repository("ssbLikeDao")
public class LikeDaoImpl implements LikeDao {
	@PersistenceContext(unitName = "blPU")
	protected EntityManager em;

	@Resource(name = "blEntityConfiguration")
	protected EntityConfiguration entityConfiguration;

	public CustomerLike load(Product product, Long customerId) {
		return load(product.getId(), customerId);
	}

	public CustomerLike load(Long productId, Long customerId) {
		final Query query = em
				.createNamedQuery("BC_READ_CustomerLike_BY_CUSTOMER_AND_PRODUCT_ID");
		query.setParameter("productId", productId);
		query.setParameter("customerId", customerId);
		@SuppressWarnings("unchecked")
		List<CustomerLike> list = query.getResultList();
		if (list == null || list.isEmpty())
			return null;
		return list.get(0);
	}

	@Override
	public CustomerLike create() {
		CustomerLike o = ((CustomerLike) entityConfiguration
				.createEntityInstance("com.ssbusy.core.like.domain.CustomerLike"));

		o.setId(null);
		return o;
	}

	@Override
	public void insert(CustomerLike customerLike) {
		em.persist(customerLike);
	}

	public void delete(CustomerLike customerLike) {
		em.remove(customerLike);
	}

	@Override
	public List<Product> load(Long customerId) {
		final Query query = em
				.createNamedQuery("BC_READ_CustomerLike_BY_CUSTOMER");
		query.setParameter("customerId", customerId);
		@SuppressWarnings("unchecked")
		List<Product> list = query.getResultList();
		if (list == null || list.isEmpty())
			return null;
		return list;
	}
}
