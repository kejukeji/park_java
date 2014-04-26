package com.ssbusy.core.christmas.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.broadleafcommerce.common.persistence.EntityConfiguration;
import org.springframework.stereotype.Repository;

import com.ssbusy.core.christmas.domain.ChristmasCard;

@Repository("christmasCardDao")
public class ChristmasCardDaoImpl implements ChristmasCardDao{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName = "blPU")
	protected EntityManager em;


	@Resource(name = "blEntityConfiguration")
	protected EntityConfiguration entityConfiguration;

	@Override
	public ChristmasCard create() {
		ChristmasCard cc = (ChristmasCard) entityConfiguration.createEntityInstance(ChristmasCard.class.getName());
		cc.setId(null);
		return cc;
	}

	@Override
	public void save(ChristmasCard christmasCard) {
		em.persist(christmasCard);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChristmasCard> loadByCustomer(Long customerId) {
		Query query = em.createQuery("select cc from com.ssbusy.core.christmas.domain.ChristmasCard cc where cc.customerId="+customerId);
		return query.getResultList();
	}

}
