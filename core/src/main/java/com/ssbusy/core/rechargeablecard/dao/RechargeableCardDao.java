package com.ssbusy.core.rechargeablecard.dao;

import java.util.List;

import com.ssbusy.core.rechargeablecard.domain.RechargeableCard;
import com.ssbusy.core.rechargeablecard.domain.RechargeableCardType;

public interface RechargeableCardDao {

	RechargeableCard create();

	List<RechargeableCard> readRechargeableCard(Long customerid);

	/**
	 * @return &gt 0表示有效卡，=0表示无效卡，&lt;0表示过期卡
	 */
	float getRechargeableCardValue(String card_id, String card_password);

	boolean tagRechargeableCardToCustomer(Long customerid, String card_id);

	RechargeableCard save(RechargeableCard card);

	RechargeableCard readRechargCardById(String reId);
	
	public RechargeableCardType getRechargeableCardTypeByCardId(String cardId);
	
	public int getUseRechargeCount(Long customerId,RechargeableCardType type);

}
