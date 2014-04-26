package com.ssbusy.admin.Statistics.service;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.broadleafcommerce.core.order.service.OrderService;
import org.springframework.stereotype.Service;

import com.ssbusy.admin.Statistics.dao.StatisticsDao;
import com.ssbusy.admin.Statistics.form.AdminOrderInfoForm;
import com.ssbusy.admin.Statistics.form.AdminUserInfoForm;
import com.ssbusy.admin.Statistics.form.ProductSalesForm;
import com.ssbusy.admin.Statistics.form.TotalSalesForm;

@Service("ssbStatisticsService")
public class StatisticsServiceImpl implements StatisticsService {

	@Resource(name = "ssbStatisticsDao")
	private StatisticsDao statisticsDao;

	@Resource(name = "ssbMyOrderService")
	private OrderService orderService;

	@Override
	public List<ProductSalesForm> getProductSalesInfoForm(Date startDate,
			Date endDate, Long locId) {
		List<ProductSalesForm> productSalesFroms = new ArrayList<ProductSalesForm>();
		List<Object> os = statisticsDao.getProductSalesInfo(startDate, endDate,
				locId);
		for (int i = 0; i < os.size(); i++) {
			String productName = (String) Array.get(os.get(i), 0);
			BigDecimal avgSalesprice = new BigDecimal((Double) Array.get(
					os.get(i), 1));
			avgSalesprice = avgSalesprice.setScale(2, BigDecimal.ROUND_HALF_UP);
			// BigDecimal avgSalesprice = new BigDecimal((Double)
			// Array.get(os.get(i), 1),new MathContext(5));
			BigDecimal avgInvoicingPrice = new BigDecimal((Double) Array.get(
					os.get(i), 3));
			avgInvoicingPrice = avgInvoicingPrice.setScale(2,
					BigDecimal.ROUND_HALF_UP);
			Long quantity = (Long) Array.get(os.get(i), 2);
			Integer saleQuantity = new Integer(quantity.toString());
			BigDecimal totalPrice = avgSalesprice.subtract(avgInvoicingPrice);
			BigDecimal allTotalPrice = totalPrice.multiply(new BigDecimal(
					saleQuantity.toString()));
			productSalesFroms.add(new ProductSalesForm(productName,
					avgSalesprice, avgInvoicingPrice, totalPrice, saleQuantity,
					allTotalPrice));
		}
		return productSalesFroms;
	}

	@Override
	public List<TotalSalesForm> getTotalSalesForm(Long[] regionIds,
			Date startDate, Date endDate) {
		// TODO 加强测试
		List<TotalSalesForm> totalSalesForms = new ArrayList<TotalSalesForm>();
		List<Object> os = statisticsDao.getTotalSalesInfo(regionIds, startDate,
				endDate);
		for (int i = 0; i < os.size(); i++) {
			Date submitDate = (Date) Array.get(os.get(i), 0);
			Date secondDate = null;
			Date thirdDate = null;
			BigDecimal firstAmount = (BigDecimal) Array.get(os.get(i), 1);
			BigDecimal secondAmount = null;
			BigDecimal thirdAmount = null;
			String firsttype = (String) Array.get(os.get(i), 2);
			String secondType = "";
			String thirdType = "";
			BigDecimal total = BigDecimal.ZERO;

			BigDecimal codPay = BigDecimal.ZERO;
			BigDecimal aliPay = BigDecimal.ZERO;
			BigDecimal bpPay = BigDecimal.ZERO;
			if ("PAYMENT_COD".equals(firsttype)) {
				codPay = firstAmount;
			} else if ("PAYMENT_ALIPAY".equals(firsttype)) {
				aliPay = firstAmount;
			} else if ("PAYMENT_BP".equals(firsttype)) {
				bpPay = firstAmount;
			}
			if (i < os.size() - 1) {
				secondDate = (Date) Array.get(os.get(i + 1), 0);
				secondAmount = (BigDecimal) Array.get(os.get(i + 1), 1);
				secondType = (String) Array.get(os.get(i + 1), 2);
			}
			if (i < os.size() - 2) {
				thirdDate = (Date) Array.get(os.get(i + 2), 0);
				thirdAmount = (BigDecimal) Array.get(os.get(i + 2), 1);
				thirdType = (String) Array.get(os.get(i + 2), 2);
			}
			if (submitDate.equals(secondDate)) {
				if ("PAYMENT_COD".equals(secondType)) {
					codPay = secondAmount;
				} else if ("PAYMENT_ALIPAY".equals(secondType)) {
					aliPay = secondAmount;
				} else if ("PAYMENT_BP".equals(secondType)) {
					bpPay = secondAmount;
				}
				i++;
			}
			if (submitDate.equals(secondDate) && submitDate.equals(thirdDate)) {
				if ("PAYMENT_COD".equals(thirdType)) {
					codPay = thirdAmount;
				} else if ("PAYMENT_ALIPAY".equals(thirdType)) {
					aliPay = thirdAmount;
				} else if ("PAYMENT_BP".equals(thirdType)) {
					bpPay = thirdAmount;
				}
				i++;
			}

			total = codPay.add(aliPay).add(bpPay);
			totalSalesForms.add(new TotalSalesForm(submitDate, total, codPay,
					aliPay, bpPay));
		}
		return totalSalesForms;
	}

	@Override
	public List<AdminUserInfoForm> getAdminUserInfoForm(Date startDate,
			Date endDate, Long id) {
		List<AdminUserInfoForm> adminUserInfoForms = new ArrayList<AdminUserInfoForm>();
		List<Object> os = statisticsDao.getUserInfo(startDate, endDate, id);
		for (int i = 0; i < os.size(); i++) {
			Long count = (Long) Array.get(os.get(i), 1);
			Long jifen = ((Integer)Array.get(os.get(i), 3)).longValue();
			Long cid = (Long) Array.get(os.get(i), 0);
			String buyCount = count.toString();
			String firstName = (String) Array.get(os.get(i), 4);
			BigDecimal totalBuy = (BigDecimal) Array.get(os.get(i), 2);
			int totalIntegral = jifen.intValue();
			adminUserInfoForms.add(new AdminUserInfoForm(cid, firstName,
					buyCount, totalBuy, totalIntegral));
		}
		return adminUserInfoForms;
	}

	@Override
	public List<AdminOrderInfoForm> getAdminOrderInfoForm(Date startDate,
			Date endDate, Long id) {
		List<AdminOrderInfoForm> adminOrderInfoForms = new ArrayList<AdminOrderInfoForm>();
		List<Object> os = statisticsDao.getAdminOrderInfo(startDate, endDate,
				id);
		for (int i = 0; i < os.size(); i++) {
			Long cid = (Long) Array.get(os.get(i), 3);
			String orderNumber = (String) Array.get(os.get(i), 0);
			String username = (String) Array.get(os.get(i), 1);
			BigDecimal total = (BigDecimal) Array.get(os.get(i), 2);
			adminOrderInfoForms.add(new AdminOrderInfoForm(cid,orderNumber,
					username, total, orderService.findOrderByOrderNumber(
							orderNumber).getOrderItems()));
		}
		return adminOrderInfoForms;
	}

}
