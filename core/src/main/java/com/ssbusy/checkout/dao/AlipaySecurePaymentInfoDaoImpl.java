package com.ssbusy.checkout.dao;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.broadleafcommerce.common.encryption.EncryptionModule;
import org.broadleafcommerce.common.persistence.EntityConfiguration;
import org.broadleafcommerce.core.payment.domain.Referenced;
import org.springframework.stereotype.Repository;

import com.ssbusy.payment.service.type.AlipayPaymentInfo;

@Repository("alipaySecurePaymentInfoDao")
public class AlipaySecurePaymentInfoDaoImpl implements
		AlipaySecurePaymentInfoDao {

	@PersistenceContext(unitName = "blSecurePU")
	protected EntityManager em;

	@Resource(name = "blEncryptionModule")
	protected EncryptionModule encryptionModule;

	@Resource(name = "blEntityConfiguration")
	protected EntityConfiguration entityConfiguration;

	@Override
	public AlipayPaymentInfo findAlipayInfo(String referenceNumber) {
		/// TODO findAlipayInfo方法暂时没用到
		return null;
	}

	@Override
	public AlipayPaymentInfo createAlipayPaymentInfo() {
		AlipayPaymentInfo response = (AlipayPaymentInfo) entityConfiguration.createEntityInstance("com.ssbusy.payment.service.type.AlipayPaymentInfo");
        response.setEncryptionModule(encryptionModule);
        return response;
	}

	@Override
	public Referenced save(Referenced securePaymentInfo) {
		// TODO save方法暂时没用到
		return null;
	}

	@Override
	public void delete(Referenced securePaymentInfo) {
		// TODO delete方法暂时没用到

	}

}
