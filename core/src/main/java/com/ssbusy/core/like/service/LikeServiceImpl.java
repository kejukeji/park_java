package com.ssbusy.core.like.service;

import java.util.List;

import javax.annotation.Resource;

import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.catalog.service.CatalogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssbusy.core.like.dao.LikeDao;
import com.ssbusy.core.like.domain.CustomerLike;
import com.ssbusy.core.product.myservice.MyProductService;

@Service("ssbLikeService")
public class LikeServiceImpl implements LikeService {

	@Resource(name = "ssbLikeDao")
	protected LikeDao likeDao;
	@Resource(name = "blCatalogService")
	CatalogService catalogService;
	@Resource(name = "ssbMyProductService")
	private MyProductService myProductService;

	@Transactional("blTransactionManager")
	public Boolean toggleLike(Long productId, Long customerId) {
		if (customerId == null || productId == null)
			return null;
		CustomerLike customerLike = likeDao.load(productId, customerId);
		Product product = catalogService.findProductById(productId);
		if (customerLike == null) {
			// TODO 有可能product或customer不存在
			if(product == null)
				return null;

			customerLike = likeDao.create();
			customerLike.setProduct(product);
			customerLike.setCustomerId(customerId);
			myProductService.addTotalLike(1L, product.getId());
			likeDao.insert(customerLike);
			return Boolean.TRUE;
		} else {
			myProductService.addTotalLike(-1L, product.getId());
			likeDao.delete(customerLike);
			return Boolean.FALSE;
		}
	}

	@Override
	public boolean queryLike(Product product, Long customerId) {
		CustomerLike customerLike = likeDao.load(product, customerId);
		return customerLike != null;
	}

	@Override
	public List<Product> showLike(Long customerId) {
		if (customerId == null)
			return null;
		List<Product> product = likeDao.load(customerId);
		if (product == null)
			return null;
		else
			return product;
	}

	@Override
	@Transactional("blTransactionManager")
	public int cancelLike(Long customerId, Long productId) {
		CustomerLike customerLike = likeDao.load(productId, customerId);
		if (customerLike == null)
			return 0;
		likeDao.delete(customerLike);
		return 1;
	}
}
