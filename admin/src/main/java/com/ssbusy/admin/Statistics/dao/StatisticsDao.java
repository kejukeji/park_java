package com.ssbusy.admin.Statistics.dao;

import java.util.Date;
import java.util.List;

public interface StatisticsDao {
	public List<Object> getProductSalesInfo(Date startDate,Date endDate,Long id);
	public List<Object> getTotalSalesInfo(Long[] regionIds,Date startDate, Date endDate);
	public List<Object> getUserInfo(Date startDate, Date endDate,Long id);
	public List<Object> getAdminOrderInfo(Date startDate, Date endDate,Long id);
}
