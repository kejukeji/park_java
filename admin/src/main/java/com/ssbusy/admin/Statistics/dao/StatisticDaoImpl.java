package com.ssbusy.admin.Statistics.dao;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.broadleafcommerce.common.persistence.EntityConfiguration;
import org.springframework.stereotype.Repository;
@Repository("ssbStatisticsDao")
public class StatisticDaoImpl implements StatisticsDao {

	@PersistenceContext(unitName = "blPU")
	protected EntityManager em;

	@Resource(name = "blEntityConfiguration")
	protected EntityConfiguration entityConfiguration;

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getProductSalesInfo(Date startDate, Date endDate,
			Long locId) {
//		Query query = em
//				.createQuery("select o.date as date,oi.name, sum(oi.quantity) as quantity,avg(oi.salePrice) from org.broadleafcommerce.core.order.domain.OrderItem oi left join oi.order o where o.customer.region.fulfillmentLocation.id="
//						+ id
//						+ " and o.status='COMPLETED' and UNIX_TIMESTAMP(o.date) >UNIX_TIMESTAMP("
//						+ startDate
//						+ ") and UNIX_TIMESTAMP(o.date) <UNIX_TIMESTAMP("
//						+ endDate
//						+ ") group by date,oi.name order by oi.name");

		Query query = em.createNamedQuery("PRODUCT_SALES_INFO");
		query.setParameter("id", locId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getTotalSalesInfo(Long[] regionIds,Date startDate, Date endDate) {
		Query query = em.createNamedQuery("TOTAL_SALES_INFO");
		List<Long> regionIdss = Arrays.asList(regionIds);
		query.setParameter("regionIds", regionIdss);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getUserInfo(Date startDate, Date endDate,Long id) {
		Query query = em.createNamedQuery("ADMIN_USER_INFO");
		query.setParameter("id", id);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getAdminOrderInfo(Date startDate, Date endDate,Long id) {
		Query query = em.createNamedQuery("ADMIN_ORDER_INFO");
		query.setParameter("id", id);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}

}
