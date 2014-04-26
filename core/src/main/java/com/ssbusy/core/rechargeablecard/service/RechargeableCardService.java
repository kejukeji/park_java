package com.ssbusy.core.rechargeablecard.service;

import java.util.Date;
import java.util.List;

import org.springframework.dao.DuplicateKeyException;

import com.ssbusy.core.rechargeablecard.domain.RechargeableCard;

public interface RechargeableCardService {
	List<RechargeableCard> getRechargeableCard(Long customerid);

	float getRechargeableCardValue(String card_id, String card_password);

	Boolean tagRechargeableCardToCustomer(Long customerid, String card_id);

	/**
	 * 创建一张新充值卡
	 * 
	 * @param cardId
	 *            nullable
	 * @param cardPassword
	 *            nullable
	 * @param expireDate
	 *            nullable, null表示永不过期
	 * @param cardValue
	 * @return new card
	 * @throws DuplicateKeyException
	 *             若传入卡号，能则有可卡号重复
	 */
	RechargeableCard createRechargeableCard(String cardId, String cardPassword,
			Date expireDate, float cardValue) throws DuplicateKeyException;

	RechargeableCard readRechargCardById(String reId);
	
	public boolean isSecondUseOnceCard(Long customerId,String cardId);

}
