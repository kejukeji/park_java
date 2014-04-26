package com.ssbusy.core.product.myservice;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;

import com.ssbusy.core.product.dao.MyProductDao;
import com.ssbusy.core.product.domain.MyProduct;

public class MyProductServiceImpl implements MyProductService{

	@Resource(name="blProductDao")
	protected MyProductDao myProductDao;
	
	@Override
	public MyProduct getTotalSale(Long productId) {
		return myProductDao.getTotalSale(productId);
	}

	@Transactional("blTransactionManager")
	@Override
	public void setTotalSale(Long quantity, Long productId) {
		myProductDao.setTotalSale(quantity, productId);
	}

	@Override
	public MyProduct getTotalLike(Long productId) {
		return myProductDao.getTotalLike(productId);
	}

	@Transactional("blTransactionManager")
	@Override
	public void addTotalLike(Long quantity, Long productId) {
		myProductDao.addTotaLike(quantity, productId);
	}

}
