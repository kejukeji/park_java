package com.ssbusy.payment.service.type;

import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.payment.domain.PaymentInfo;
import org.broadleafcommerce.core.payment.domain.PaymentInfoImpl;
import org.broadleafcommerce.core.payment.service.PaymentInfoFactory;
import org.springframework.stereotype.Service;

@Service("alipayPaymentInfoFactory")
public class AlipayPaymentInfoFactoryImpl implements PaymentInfoFactory {
	public PaymentInfo constructPaymentInfo(Order order) {
		PaymentInfoImpl paymentInfo = new PaymentInfoImpl();
		paymentInfo.setOrder(order);
		paymentInfo.setType(MyPaymentInfoType.Payment_Alipay);
		paymentInfo.setReferenceNumber(String.valueOf(order.getId()));
		paymentInfo.setAmount(order.getRemainingTotal());
		return paymentInfo;
	}
}
