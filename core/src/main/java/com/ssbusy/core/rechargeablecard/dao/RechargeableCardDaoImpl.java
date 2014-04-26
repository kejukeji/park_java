package com.ssbusy.core.rechargeablecard.dao;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.broadleafcommerce.common.persistence.EntityConfiguration;
import org.springframework.stereotype.Repository;

import com.ssbusy.core.rechargeablecard.domain.RechargeableCard;
import com.ssbusy.core.rechargeablecard.domain.RechargeableCardImpl;
import com.ssbusy.core.rechargeablecard.domain.RechargeableCardType;

@Repository("ssbRechargeableCardDao")
public class RechargeableCardDaoImpl implements RechargeableCardDao {
	
	@PersistenceContext(unitName = "blPU")
	protected EntityManager em;

	@Resource(name = "blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

	@Override
	public RechargeableCard create() {
		return ((RechargeableCard) entityConfiguration
				.createEntityInstance("com.ssbusy.core.rechargeablecard.domain.RechargeableCard"));
	}

	@Override
	public RechargeableCard save(RechargeableCard card) {
		return em.merge(card);
	}

	@SuppressWarnings("unchecked")
	public List<RechargeableCard> readRechargeableCard(Long customerid) {
		final Query query = em.createNamedQuery("SSB_READ_RECHARGEABLECARDS");
	  query.setParameter("customerid", customerid);
	  List<RechargeableCard> cards = query.getResultList();
	 	return cards;
	}
 
	@SuppressWarnings("unchecked")
	public float getRechargeableCardValue(String card_id, String card_password) {
		final Query query = em
				.createNamedQuery("SSB_FIND_RECHARGEABLECARDS_NOT_USED");
		query.setParameter("card_id", card_id);
		query.setParameter("card_password", card_password);
		List<RechargeableCard> cards = query.getResultList();
		if (!cards.isEmpty()) {
			RechargeableCard card = cards.get(0);
			if (card.getExpireDate() != null
					&& new Date().after(card.getExpireDate()))
				// 过期卡，返回负值
				return -card.getCard_value();
			return card.getCard_value();
		}
		return 0;
	}
		
	public boolean tagRechargeableCardToCustomer(Long customerid,String card_id){
		Query query = em.createNamedQuery("TAG_CARD_TO_CUSTOMER");
		query.setParameter("card_id", card_id);
		query.setParameter("customerId", customerid);
		return query.executeUpdate() > 0;
	}

	@Override
	public RechargeableCard readRechargCardById(String reId) {
		return em.find(RechargeableCardImpl.class, reId);
	}

	@Override
	public RechargeableCardType getRechargeableCardTypeByCardId(String cardId) {
		RechargeableCard card=em.find(com.ssbusy.core.rechargeablecard.domain.RechargeableCardImpl.class, cardId);
		if(card.getCardType()!=null)
			return card.getCardType();
		return RechargeableCardType.NORMAL;
	}
	
	public int getUseRechargeCount(Long customerId,RechargeableCardType type){
		Query query =em.createNamedQuery("SSB_READ_RECHARGECOUNT_BY_ID_AND_TYPE");
		query.setParameter("customerId", customerId);
		query.setParameter("type", type.getType());
		Long count = (Long)query.getSingleResult();
		
		return count.intValue();
	}
}
