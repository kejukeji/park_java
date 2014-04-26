package com.ssbusy.payment.service.type;

import javax.annotation.Resource;

import org.broadleafcommerce.core.payment.domain.Referenced;
import org.broadleafcommerce.core.payment.service.SecurePaymentInfoService;
import org.broadleafcommerce.core.payment.service.type.PaymentInfoType;
import org.broadleafcommerce.core.workflow.WorkflowException;
import org.springframework.stereotype.Service;

import com.ssbusy.checkout.dao.AlipaySecurePaymentInfoDao;

@Service("alipaySecurePaymentInfoService")
public class AlipayPaymentInfoServiceImpl implements SecurePaymentInfoService {

	@Resource(name = "alipaySecurePaymentInfoDao")
	protected AlipaySecurePaymentInfoDao securePaymentInfoDao;

	@Override
	public Referenced findSecurePaymentInfo(String referenceNumber,
			PaymentInfoType paymentInfoType) throws WorkflowException {
		// TODO find 未用
		return null;
	}

	@Override
	public Referenced save(Referenced securePaymentInfo) {
		// TODO save 未用
		return null;
	}

	@Override
	public Referenced create(PaymentInfoType paymentInfoType) {
		if (paymentInfoType.equals(MyPaymentInfoType.Payment_Alipay)) {
			AlipayPaymentInfo alipayInfo = securePaymentInfoDao.createAlipayPaymentInfo();
			return alipayInfo;
		} else
			return null;
	}

	@Override
	public void remove(Referenced securePaymentInfo) {
		// TODO 未用

	}

	@Override
	public void findAndRemoveSecurePaymentInfo(String referenceNumber,
			PaymentInfoType paymentInfoType) throws WorkflowException {
		// TODO 未用

	}

}
