package com.ssbusy.api.endpoint.checkout;

import java.util.Map;

import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.core.payment.domain.PaymentResponseItem;
import org.broadleafcommerce.core.payment.domain.Referenced;
import org.broadleafcommerce.core.payment.service.PaymentContext;
import org.broadleafcommerce.core.payment.service.exception.PaymentException;
import org.broadleafcommerce.core.payment.service.module.AbstractModule;
import org.broadleafcommerce.core.payment.service.type.PaymentInfoType;
 class AfPaymentModule extends  AbstractModule{

	@Override
	public Boolean isValidCandidate(PaymentInfoType paymentType) {
		return MyPaymentInfoType.Payment_after_ArrivalGoods.equals(paymentType);
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
		 Map<String, String> additionalFields = validateAf(paymentContext);
	        responseItem.setTransactionSuccess(additionalFields != null);
	        if (responseItem.getTransactionSuccess()) {
	            findPaymentInfoFromContext(paymentContext).setAdditionalFields(additionalFields);
	        } else {
	            throw new PaymentException("Problem Af.");
	        }
	        return responseItem;
	}

	private Map<String, String> validateAf(PaymentContext paymentContext) {
		Referenced af=paymentContext.getReferencedPaymentInfo();
		if(af==null){
			return null;
		}
		
		return null;
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
		  PaymentResponseItem authorizeResponseItem = authorize(paymentContext);
	        if (authorizeResponseItem.getTransactionSuccess()) {
	            return debit(paymentContext);
	        } else {
	            throw new PaymentException("Problem processing Af.");
	        }
	}

	@Override
	public PaymentResponseItem processCredit(PaymentContext paymentContext,
			Money amountToCredit, PaymentResponseItem responseItem)
			throws PaymentException {
		responseItem.setTransactionSuccess(true);
		 Map<String, String> additionalFields = validateAf(paymentContext);
	        findPaymentInfoFromContext(paymentContext).setAdditionalFields(additionalFields);
	        return responseItem;
		
	}

	@Override
	public PaymentResponseItem processVoidPayment(
			PaymentContext paymentContext, Money amountToVoid,
			PaymentResponseItem responseItem) throws PaymentException {
		  if (amountToVoid.greaterThan(Money.ZERO)){
	            return credit(paymentContext);
	        } else {
	            return reverseAuthorize(paymentContext);
	        }
	}

	@Override
	public PaymentResponseItem processBalance(PaymentContext paymentContext,
			PaymentResponseItem responseItem) throws PaymentException {
		  throw new PaymentException("balance not implemented.");
	}

	@Override
	public PaymentResponseItem processPartialPayment(
			PaymentContext paymentContext, Money amountToDebit,
			PaymentResponseItem responseItem) throws PaymentException {
		   throw new PaymentException("partial payment not implemented.");
	}

}
