package com.ssbusy.payment.service.type;

import javax.annotation.Resource;

import org.broadleafcommerce.core.payment.domain.Referenced;
import org.broadleafcommerce.core.payment.service.SecurePaymentInfoService;
import org.broadleafcommerce.core.payment.service.type.PaymentInfoType;
import org.broadleafcommerce.core.workflow.WorkflowException;

import com.ssbusy.checkout.dao.MySecurePaymentInfoDao;


public class MySecurepaymentinfoServiceImpl implements SecurePaymentInfoService  {

	    @Resource(name = "blSecurePaymentInfoDao")
	    protected MySecurePaymentInfoDao securePaymentInfoDao;

	    public Referenced save(Referenced securePaymentInfo) {
	        return securePaymentInfoDao.save(securePaymentInfo);
	    }

	    public Referenced create(PaymentInfoType paymentInfoType) {
	        if (paymentInfoType.equals(MyPaymentInfoType.Payment_Cod)) {
	            CodPaymentInfo ccinfo = securePaymentInfoDao.createCodPaymentInfo();
	            return ccinfo;
	        } else 
	        return null;
	    }

	    public Referenced findSecurePaymentInfo(String referenceNumber,PaymentInfoType paymentInfoType) throws WorkflowException {
	        if (paymentInfoType == MyPaymentInfoType.Payment_Cod) {
	            CodPaymentInfo ccinfo =findCodInfo(referenceNumber); 
	            if (ccinfo == null) {
	                throw new WorkflowException("No cod info associated with cod payment type with reference number: " + referenceNumber);
	            }
	            return ccinfo;
	          }  else 

	        return null;
	    }

	    public void findAndRemoveSecurePaymentInfo(String referenceNumber, PaymentInfoType paymentInfoType) throws WorkflowException {
	        Referenced referenced = findSecurePaymentInfo(referenceNumber, paymentInfoType);
	        if (referenced != null) {
	            remove(referenced);
	        }

	    }

	    public void remove(Referenced securePaymentInfo) {
	        securePaymentInfoDao.delete(securePaymentInfo);
	    }

	  
	    
	    /***/
	    protected CodPaymentInfo findCodInfo(String referenceNumber) {
	        return  securePaymentInfoDao.findCodInfo(referenceNumber);
	    }

		
}
