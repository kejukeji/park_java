package com.ssbusy.payment.service.type;

import org.broadleafcommerce.core.payment.service.type.PaymentInfoType;
/*
 * MyPaymentInfoType继承了PaymentInfoType
 * add cod type
 * 
 * 
 * 
 * 
 * */
public class MyPaymentInfoType extends  PaymentInfoType{
	  
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final PaymentInfoType Payment_Cod= new PaymentInfoType("PAYMENT_COD", "货到付款");
	public static final PaymentInfoType Payment_Bp= new PaymentInfoType("PAYMENT_BP", "余额支付"); 
	public static final PaymentInfoType Payment_Integrl= new PaymentInfoType("PAYMENT_INTEGRL", "仙丹兑换"); 
	public static final PaymentInfoType Payment_Alipay= new PaymentInfoType("PAYMENT_ALIPAY", "支付宝付"); 
	
}