package com.ssbusy.admin.Statistics.service;

import java.util.Date;
import java.util.List;

import com.ssbusy.admin.Statistics.form.AdminOrderInfoForm;
import com.ssbusy.admin.Statistics.form.AdminUserInfoForm;
import com.ssbusy.admin.Statistics.form.ProductSalesForm;
import com.ssbusy.admin.Statistics.form.TotalSalesForm;

public interface StatisticsService {

	public List<ProductSalesForm> getProductSalesInfoForm(Date startDate, Date endDate,
			Long id);
	public List<TotalSalesForm> getTotalSalesForm(Long[] regionIds,Date startDate, Date endDate);
	
	public List<AdminUserInfoForm> getAdminUserInfoForm(Date startDate, Date endDate,Long id);
	
	public List<AdminOrderInfoForm> getAdminOrderInfoForm(Date startDate, Date endDate,Long id);
}
