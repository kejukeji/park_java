package com.ssbusy.checkout.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.broadleafcommerce.common.encryption.EncryptionModule;
import org.broadleafcommerce.common.persistence.EntityConfiguration;
import org.broadleafcommerce.core.payment.domain.Referenced;
import org.springframework.stereotype.Repository;
import com.ssbusy.payment.service.type.IntegrlPaymentInfo;
@Repository("blIntegrlSecurePaymentInfoDao")
public class InSecurePaymentInfoDaoImpl implements InSecurePaymentInfoDao {
  

	  @PersistenceContext(unitName = "blSecurePU")
	    protected EntityManager em;

	    @Resource(name = "blEncryptionModule")
	    protected EncryptionModule encryptionModule;

	    @Resource(name = "blEntityConfiguration")
	    protected EntityConfiguration entityConfiguration;
	    
	
	@SuppressWarnings("unchecked")
	@Override
	public IntegrlPaymentInfo findInInfo(String referenceNumber) {
		Query query = em.createNamedQuery("BC_READ_BANK_ACCOUNT_BY_REFERENCE_NUMBER");
        query.setParameter("referenceNumber", referenceNumber);
        List<IntegrlPaymentInfo> infos = query.getResultList();
        IntegrlPaymentInfo response = (infos == null || infos.size() == 0) ? null : infos.get(0);
        if (response != null) {
            response.setEncryptionModule(encryptionModule);
        }
        return response;
	}

	@Override
	public IntegrlPaymentInfo createInPaymentInfo() {
		IntegrlPaymentInfo response = (IntegrlPaymentInfo) entityConfiguration.createEntityInstance("com.ssbusy.payment.service.type.IntegrlPaymentInfo");
        response.setEncryptionModule(encryptionModule);
        return response;
	}

	@Override
	public Referenced save(Referenced securePaymentInfo) {
	
		 return em.merge(securePaymentInfo);
	}

	@Override
	public void delete(Referenced securePaymentInfo) {
		   if (!em.contains(securePaymentInfo)) {
	            securePaymentInfo = em.find(securePaymentInfo.getClass(), securePaymentInfo.getId());
	        }
	        em.remove(securePaymentInfo);
		
	}


	
}
