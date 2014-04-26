package com.ssbusy.core.inneraddress.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.ssbusy.core.domain.AreaAddress;
import com.ssbusy.core.domain.AreaAddressImpl;
import com.ssbusy.core.region.domain.Region;

@Repository("ssbAreaDao")
public class AreaDaoImpl implements AreaDao{

	
	@PersistenceContext(unitName = "blPU")
	protected EntityManager em;

	@Override
	public List<AreaAddress> getAreaByRegion(Region region) {
		final Query query=em.createNamedQuery("SSB_AREAADDRESS_BY_REGION");
		query.setParameter("region", region);
		@SuppressWarnings("unchecked")
		List<AreaAddress> list = query.getResultList();
		if(list.size()>0)
			return list;
		else return null;
	}
	@Override
	public AreaAddress getAreaById(Long areaId) {
		return em.find(AreaAddressImpl.class, areaId);
	}
	
}
