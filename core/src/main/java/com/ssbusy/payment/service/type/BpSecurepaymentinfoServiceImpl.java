package com.ssbusy.payment.service.type;

import javax.annotation.Resource;

import org.broadleafcommerce.core.payment.domain.Referenced;
import org.broadleafcommerce.core.payment.service.SecurePaymentInfoService;
import org.broadleafcommerce.core.payment.service.type.PaymentInfoType;
import org.broadleafcommerce.core.workflow.WorkflowException;
import org.springframework.stereotype.Service;

import com.ssbusy.checkout.dao.BpSecurePaymentInfoDao;

@Service("blBpSecurePaymentInfoService")
public class BpSecurepaymentinfoServiceImpl implements SecurePaymentInfoService {

	@Resource(name = "blbpSecurePaymentInfoDao")
	protected BpSecurePaymentInfoDao securePaymentInfoDao;

	@Override
	public Referenced findSecurePaymentInfo(String referenceNumber,
			PaymentInfoType paymentInfoType) throws WorkflowException {
		if (paymentInfoType == MyPaymentInfoType.Payment_Bp) {
			BpPaymentInfo bpinfo = findBpInfo(referenceNumber);
			if (bpinfo == null) {
				throw new WorkflowException(
						"No cod info associated with cod payment type with reference number: "
								+ referenceNumber);
			}
			return bpinfo;
		} else
			return null;
	}

	@Override
	public Referenced save(Referenced securePaymentInfo) {
		return securePaymentInfoDao.save(securePaymentInfo);
	}

	@Override
	public Referenced create(PaymentInfoType paymentInfoType) {
		if (paymentInfoType.equals(MyPaymentInfoType.Payment_Bp)) {
			BpPaymentInfo bpinfo = securePaymentInfoDao.createBpPaymentInfo();
			return bpinfo;
		} else
			return null;
	}

	@Override
	public void remove(Referenced securePaymentInfo) {
		securePaymentInfoDao.delete(securePaymentInfo);
	}

	@Override
	public void findAndRemoveSecurePaymentInfo(String referenceNumber,
			PaymentInfoType paymentInfoType) throws WorkflowException {
		Referenced referenced = findSecurePaymentInfo(referenceNumber,
				paymentInfoType);
		if (referenced != null) {
			remove(referenced);
		}
	}

	protected BpPaymentInfo findBpInfo(String referenceNumber) {
		return securePaymentInfoDao.findBpInfo(referenceNumber);
	}

}
