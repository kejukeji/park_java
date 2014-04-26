package com.ssbusy.module;

import javax.annotation.Resource;

import org.broadleafcommerce.common.currency.domain.BroadleafCurrency;
import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.core.payment.domain.PaymentResponseItem;
import org.broadleafcommerce.core.payment.service.PaymentContext;
import org.broadleafcommerce.core.payment.service.exception.PaymentException;
import org.broadleafcommerce.core.payment.service.module.AbstractModule;
import org.broadleafcommerce.core.payment.service.type.PaymentInfoType;

import com.ssbusy.core.integral.service.IntegralService;
import com.ssbusy.payment.service.type.MyPaymentInfoType;

public class IntegrlPaymentModule extends AbstractModule{
     
	@Resource(name="ssbIntegralService")
	private IntegralService integrlservice;
	
	@Override
	public Boolean isValidCandidate(PaymentInfoType paymentType) {
		return MyPaymentInfoType.Payment_Integrl.equals(paymentType);
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
		   Money total=paymentContext.getPaymentInfo().getOrder().getTotal();		  
		   Long customerid=paymentContext.getPaymentInfo().getOrder().getCustomer().getId();
		   BroadleafCurrency currency=paymentContext.getPaymentInfo().getOrder().getCurrency();
		  int  integrl=integrlservice.viewCustomerIntegral(customerid);
		  int  ototal=total.getAmount().intValue();
		  if(ototal<=integrl){
			  integrlservice.payByIntegrl(customerid, ototal);
			  responseItem.setTransactionSuccess(true);
			  responseItem.setTransactionAmount(total);
			  
		  }else{
			  responseItem.setTransactionSuccess(false);
			  Money m=new Money(currency);
			  responseItem.setTransactionAmount(m);
		  }
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
		if (amountToVoid.greaterThan(Money.ZERO)){
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
