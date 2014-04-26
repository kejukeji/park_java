package com.ssbusy.core.region.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.broadleafcommerce.common.persistence.EntityConfiguration;
import org.springframework.stereotype.Repository;

import com.ssbusy.core.region.domain.Region;
import com.ssbusy.core.region.domain.RegionImpl;
@Repository("ssbRegionDao")
public class RegionDaoImpl implements RegionDao{
	 @PersistenceContext(unitName = "blPU")
	    protected EntityManager em;
	    @Resource(name = "blEntityConfiguration")
	    protected EntityConfiguration entityConfiguration;
	 @SuppressWarnings("unchecked")
	public List<Region> getRegion(){
		final Query query = em.createNamedQuery("BC_READ_REGION");
		return query.getResultList();		 
	 }
	 public Region getRegion(Long id){
		return em.find(RegionImpl.class,id);			
	 }

}
