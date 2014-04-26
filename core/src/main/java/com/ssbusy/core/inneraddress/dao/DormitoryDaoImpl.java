package com.ssbusy.core.inneraddress.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.ssbusy.core.domain.AreaAddress;
import com.ssbusy.core.domain.Dormitory;
import com.ssbusy.core.domain.DormitoryImpl;

@Repository("ssbDormitoryDao")
public class DormitoryDaoImpl implements DormitoryDao {

	
	@PersistenceContext(unitName = "blPU")
	protected EntityManager em;

	@Override
	public List<Dormitory> getDormitoryByArea(AreaAddress areaAddress) {
		final Query query=em.createNamedQuery("SSB_DORMITORY_BY_AREAADDRESS");
		query.setParameter("areaAddress", areaAddress);
		@SuppressWarnings("unchecked")
		List<Dormitory> list = query.getResultList();
		if(list.size()>0)
			return list;
		else return null;
	}

	@Override
	public Dormitory loadDormitoryById(Long dormitoryId) {
		return em.find(DormitoryImpl.class, dormitoryId);
	}

}
