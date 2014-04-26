package com.ssbusy.core.christmas.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssbusy.core.christmas.dao.ChristmasCardDao;
import com.ssbusy.core.christmas.domain.ChristmasCard;

@Service("christmasCardService")
public class ChristmasCardServiceImpl implements ChristmasCardService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Resource(name="christmasCardDao")
	protected ChristmasCardDao christmasCardDao;
	@Override
	public List<ChristmasCard> loadChristmasCard(Long customerId) {
		return christmasCardDao.loadByCustomer(customerId);
	}

	@Override
	@Transactional("blTransactionManager")
	public ChristmasCard persist(Long customerId, int signDate) {
		ChristmasCard cc = christmasCardDao.create();
		cc.setCustomerId(customerId);
		cc.setSignDate(signDate);
		christmasCardDao.save(cc);
		return cc;
	}

}
