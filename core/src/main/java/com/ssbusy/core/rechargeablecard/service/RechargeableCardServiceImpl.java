package com.ssbusy.core.rechargeablecard.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.broadleafcommerce.core.order.service.type.OrderStatus;
import org.broadleafcommerce.core.payment.service.type.PaymentInfoType;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssbusy.core.myorder.domain.MyOrderStatus;
import com.ssbusy.core.rechargeablecard.dao.RechargeableCardDao;
import com.ssbusy.core.rechargeablecard.domain.RechargeableCard;
import com.ssbusy.core.rechargeablecard.domain.RechargeableCardType;
import com.ssbusy.helper.StringHelper;
import com.ssbusy.payment.service.type.MyPaymentInfoType;

@Service("ssbRechargeableCardService")
public class RechargeableCardServiceImpl implements RechargeableCardService {
	@Resource(name = "ssbRechargeableCardDao")
	protected RechargeableCardDao rechargeableCardDao;

	@SuppressWarnings("unused")
	public RechargeableCardServiceImpl() {
		OrderStatus status = MyOrderStatus.COMPLETED;
		PaymentInfoType type = MyPaymentInfoType.Payment_Cod;
	}

	public List<RechargeableCard> getRechargeableCard(Long customerid) {
		return rechargeableCardDao.readRechargeableCard(customerid);
	}

	/**
	 * @return &gt 0表示有效卡，=0表示无效卡(不存在，或已使用)，&lt;0表示过期卡
	 */
	public float getRechargeableCardValue(String card_id, String card_password) {
		return rechargeableCardDao.getRechargeableCardValue(card_id,
				card_password);
	}

	@Transactional("blTransactionManager")
	public Boolean tagRechargeableCardToCustomer(Long customerid, String card_id) {
		return rechargeableCardDao.tagRechargeableCardToCustomer(customerid,
				card_id);
	}

	@Override
	@Transactional("blTransactionManager")
	public RechargeableCard createRechargeableCard(String cardId,
			String cardPassword, Date expireDate, float cardValue)
			throws DuplicateKeyException {
		RechargeableCard card = rechargeableCardDao.create();
		card.setCard_id(StringUtils.isEmpty(cardId) ? StringHelper
				.randomNumber(4) : cardId);
		card.setCard_password(StringUtils.isEmpty(cardPassword) ? StringHelper
				.randomNumber(2) : cardPassword);
		card.setCard_value(cardValue);
		card.setExpireDate(expireDate);

		return rechargeableCardDao.save(card);
	}

	@Override
	public RechargeableCard readRechargCardById(String reId) {
		
		return rechargeableCardDao.readRechargCardById(reId);
	}

	public boolean isSecondUseOnceCard(Long customerId,String cardId) {
		
		RechargeableCardType type = rechargeableCardDao.getRechargeableCardTypeByCardId(cardId);
		
		int onceCardCount = rechargeableCardDao.getUseRechargeCount(customerId, RechargeableCardType.ONCEFORCUSTOMER);
		
		if(type.equals(RechargeableCardType.ONCEFORCUSTOMER)&&onceCardCount>0)
			return true;
		return false;
	}

	
}
