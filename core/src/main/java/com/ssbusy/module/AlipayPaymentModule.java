package com.ssbusy.module;

import java.util.Date;

import org.broadleafcommerce.common.currency.domain.BroadleafCurrency;
import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.core.payment.domain.PaymentResponseItem;
import org.broadleafcommerce.core.payment.service.PaymentContext;
import org.broadleafcommerce.core.payment.service.exception.PaymentException;
import org.broadleafcommerce.core.payment.service.module.AbstractModule;
import org.broadleafcommerce.core.payment.service.type.PaymentInfoType;

import com.ssbusy.payment.service.type.MyPaymentInfoType;

public class AlipayPaymentModule extends AbstractModule{

	@Override
	public Boolean isValidCandidate(PaymentInfoType paymentType) {
		return MyPaymentInfoType.Payment_Alipay.equals(paymentType);
	}

	@Override
	public PaymentResponseItem processReverseAuthorize(
			PaymentContext paymentContext, Money amountToReverseAuthorize,
			PaymentResponseItem responseItem) throws PaymentException {
		return responseItem;
	}

	@Override
	public PaymentResponseItem processAuthorize(PaymentContext paymentContext,
			Money amountToAuthorize, PaymentResponseItem responseItem)
			throws PaymentException {
		return responseItem;
	}

	@Override
	public PaymentResponseItem processDebit(PaymentContext paymentContext,
			Money amountToDebit, PaymentResponseItem responseItem)
			throws PaymentException {
		return responseItem;
	}

	@Override
	public PaymentResponseItem processAuthorizeAndDebit(
			PaymentContext paymentContext, Money amountToDebit,
			PaymentResponseItem responseItem) throws PaymentException {
		//TODO Alipay支付成功与否在这里设置。
		Money total = paymentContext.getPaymentInfo().getAmount();
		BroadleafCurrency currency = paymentContext.getPaymentInfo().getOrder()
				.getCurrency();
		responseItem.setTransactionTimestamp(new Date());
		responseItem.setTransactionSuccess(true);
		responseItem.setTransactionAmount(total);
		responseItem.setCurrency(currency);
		return responseItem;
	}

	@Override
	public PaymentResponseItem processCredit(PaymentContext paymentContext,
			Money amountToCredit, PaymentResponseItem responseItem)
			throws PaymentException {
		return responseItem;
	}

	@Override
	public PaymentResponseItem processVoidPayment(
			PaymentContext paymentContext, Money amountToVoid,
			PaymentResponseItem responseItem) throws PaymentException {
		if (amountToVoid.greaterThan(Money.ZERO)) {
			return credit(paymentContext);
		} else {
			return reverseAuthorize(paymentContext);
		}
	}

	@Override
	public PaymentResponseItem processBalance(PaymentContext paymentContext,
			PaymentResponseItem responseItem) throws PaymentException {
		return responseItem;
	}

	@Override
	public PaymentResponseItem processPartialPayment(
			PaymentContext paymentContext, Money amountToDebit,
			PaymentResponseItem responseItem) throws PaymentException {
		return responseItem;
	}
	
	

}
