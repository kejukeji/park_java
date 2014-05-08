package com.ssbusy.core.carbarn.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.broadleafcommerce.common.persistence.EntityConfiguration;
import org.springframework.stereotype.Repository;

import com.ssbusy.core.carbarn.domain.Carbarn;
import com.ssbusy.core.carbarn.domain.CarbarnImpl;

/**
 * 
 * @author song
 * 
 */
@Repository("carbarnDao")
public class CarbarnDaoImpl implements CarbarnDao {

	@PersistenceContext(unitName = "blPU")
	protected EntityManager em;

	@Resource(name = "blEntityConfiguration")
	protected EntityConfiguration entityConfiguration;

	@SuppressWarnings("unchecked")
	@Override
	public List<Carbarn> readCarbarnByCarbarnName(String carbarnName) {
		Query query = em.createNamedQuery("READ_CARBARN_BY_CARBARN_NAME");
		query.setParameter("carbarnName", '%' + carbarnName + '%');
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Carbarn> readCarbarnByCarbarnAddress(String carbarnAddress) {
		Query query = em.createNamedQuery("READ_CARBARN_BY_CARBARN_ADDRESS");
		query.setParameter("carbarnAddress", '%' + carbarnAddress + '%');
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Carbarn> readCarbarnByLatitudeAndLongitude(
			Double beginLatitude, Double endLatitude, Double beginLongitude,
			Double endLongitude) {
		Query query = em.createNamedQuery("READ_CARBARN_BY_LATITUDE_AND_LONGITUDE");
		query.setParameter("beginLatitude", beginLatitude);
		query.setParameter("endLatitude", endLatitude);
		query.setParameter("beginLongitude", beginLongitude);
		query.setParameter("endLongitude", endLongitude);
		return query.getResultList();
	}

	@Override
	public Carbarn readCarbarnById(Long id) {
		return em.find(CarbarnImpl.class, id);
	}

	@Override
	public Carbarn updateCarbarn(Carbarn carbarn) {
		Carbarn resCarbarn = em.merge(carbarn);
		return resCarbarn;
	}
	


}
