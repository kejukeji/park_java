package com.ssbusy.payment.service.type;

import javax.annotation.Resource;

import org.broadleafcommerce.core.payment.domain.Referenced;
import org.broadleafcommerce.core.payment.service.SecurePaymentInfoService;
import org.broadleafcommerce.core.payment.service.type.PaymentInfoType;
import org.broadleafcommerce.core.workflow.WorkflowException;
import org.springframework.stereotype.Service;

import com.ssbusy.checkout.dao.InSecurePaymentInfoDao;
@Service("blInSecurePaymentInfoService")
public class InSecurePaymentInfoServiceImpl implements SecurePaymentInfoService{

	
	 @Resource(name = "blIntegrlSecurePaymentInfoDao")
	    protected InSecurePaymentInfoDao securePaymentInfoDao;
	@Override
	public Referenced findSecurePaymentInfo(String referenceNumber,
			PaymentInfoType paymentInfoType) throws WorkflowException {
		 if (paymentInfoType == MyPaymentInfoType.Payment_Integrl) {
	           IntegrlPaymentInfo ininfo = findInInfo(referenceNumber); 
	            if (ininfo == null) {
	                throw new WorkflowException("No cod info associated with In payment type with reference number: " + referenceNumber);
	            }
	            return ininfo;
	          }  else 

	        return null;
	}

	@Override
	public Referenced save(Referenced securePaymentInfo) {
		return securePaymentInfoDao.save(securePaymentInfo);
	}

	@Override
	public Referenced create(PaymentInfoType paymentInfoType) {
		 if (paymentInfoType.equals(MyPaymentInfoType.Payment_Integrl)) {
	          IntegrlPaymentInfo ininfo = securePaymentInfoDao.createInPaymentInfo();
	            return ininfo;
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
		   Referenced referenced = findSecurePaymentInfo(referenceNumber, paymentInfoType);
	        if (referenced != null) {
	            remove(referenced);
	        }
		
	}
	
	  protected IntegrlPaymentInfo findInInfo(String referenceNumber) {
	        return  securePaymentInfoDao.findInInfo(referenceNumber);
	    }


}
