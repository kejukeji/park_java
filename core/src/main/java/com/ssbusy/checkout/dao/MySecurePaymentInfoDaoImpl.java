package com.ssbusy.checkout.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.broadleafcommerce.common.encryption.EncryptionModule;
import org.broadleafcommerce.common.persistence.EntityConfiguration;
import org.broadleafcommerce.core.payment.domain.Referenced;

import com.ssbusy.payment.service.type.CodPaymentInfo;

/*
 * 
 * MySecurePaymentInfoDaoImpl implements MySecurePaymentInfoDao
 * one create 
 * 
 * 
 * 
 * 
 * */

public class MySecurePaymentInfoDaoImpl implements MySecurePaymentInfoDao {

	@PersistenceContext(unitName = "blSecurePU")
	protected EntityManager em;

	@Resource(name = "blEncryptionModule")
	protected EncryptionModule encryptionModule;

	@Resource(name = "blEntityConfiguration")
	protected EntityConfiguration entityConfiguration;

	public Referenced save(Referenced securePaymentInfo) {
		return em.merge(securePaymentInfo);
	}

	@Override
	public CodPaymentInfo findCodInfo(String referenceNumber) {

		// CodPaymentInfo response = (CodPaymentInfo)
		// entityConfiguration.createEntityInstance("com.ssbusy.payment.service.type.CodPaymentInfo");
		// response.setEncryptionModule(encryptionModule);

		// Query query =
		// em.createNamedQuery("BC_READ_BANK_ACCOUNT_BY_REFERENCE_NUMBER");
		// query.setParameter("referenceNumber", referenceNumber);
		// List<BankAccountPaymentInfo> infos = query.getResultList();
		//
		// if (response != null) {
		// response.setEncryptionModule(encryptionModule);
		// }
		// return response;

		Query query = em
				.createNamedQuery("BC_READ_BANK_ACCOUNT_BY_REFERENCE_NUMBER");
		query.setParameter("referenceNumber", referenceNumber);
		@SuppressWarnings("unchecked")
		List<CodPaymentInfo> infos = query.getResultList();
		CodPaymentInfo response = (infos == null || infos.size() == 0) ? null
				: infos.get(0);
		if (response != null) {
			response.setEncryptionModule(encryptionModule);
		}
		return response;

	}

	@Override
	public CodPaymentInfo createCodPaymentInfo() {
		CodPaymentInfo response = (CodPaymentInfo) entityConfiguration
				.createEntityInstance("com.ssbusy.payment.service.type.CodPaymentInfo");
		response.setEncryptionModule(encryptionModule);
		return response;
	}

	@Override
	public void delete(Referenced securePaymentInfo) {
		if (!em.contains(securePaymentInfo)) {
			securePaymentInfo = em.find(securePaymentInfo.getClass(),
					securePaymentInfo.getId());
		}
		em.remove(securePaymentInfo);
	}

}
