package com.ssbusy.module;

import org.broadleafcommerce.common.currency.domain.BroadleafCurrency;
import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.core.payment.domain.PaymentResponseItem;
import org.broadleafcommerce.core.payment.service.PaymentContext;
import org.broadleafcommerce.core.payment.service.exception.PaymentException;
import org.broadleafcommerce.core.payment.service.module.AbstractModule;
import org.broadleafcommerce.core.payment.service.type.PaymentInfoType;

import com.ssbusy.payment.service.type.MyPaymentInfoType;

public  class CodPaymentModule extends  AbstractModule{

	@Override
	public Boolean isValidCandidate(PaymentInfoType paymentType) {
		return MyPaymentInfoType.Payment_Cod.equals(paymentType);
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
		   
//	        if (authorizeResponseItem.getTransactionSuccess()) {
//	           
//	        	return responseItem; 
//	        } else {
//	            throw new PaymentException("Problem processing Af.");
//	        }
		     BroadleafCurrency currency=paymentContext.getPaymentInfo().getOrder().getCurrency();
		     responseItem.setTransactionAmount(paymentContext.getPaymentInfo().getAmount());
		     responseItem.setCurrency(currency);
		     responseItem.setTransactionSuccess(true);
		     return responseItem;
	        
	}

	@Override
	public PaymentResponseItem processCredit(PaymentContext paymentContext,
			Money amountToCredit, PaymentResponseItem responseItem)
			throws PaymentException {
		   responseItem.setTransactionSuccess(true);
	        return responseItem;
		
	}


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
	public PaymentResponseItem processPartialPayment(
			PaymentContext paymentContext, Money amountToDebit,
			PaymentResponseItem responseItem) throws PaymentException {
		   throw new PaymentException("partial payment not implemented.");
	}





	@Override
	public PaymentResponseItem processReverseAuthorize(
			PaymentContext paymentContext, Money amountToReverseAuthorize,
			PaymentResponseItem responseItem) throws PaymentException {
		
	        return responseItem;
	}



	@Override
	public PaymentResponseItem processBalance(PaymentContext paymentContext,
			PaymentResponseItem responseItem) throws PaymentException {
		return null;
	}

}
