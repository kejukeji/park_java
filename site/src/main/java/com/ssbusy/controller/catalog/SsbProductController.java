package com.ssbusy.controller.catalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.broadleafcommerce.common.web.controller.BroadleafAbstractController;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.catalog.domain.Sku;
import org.broadleafcommerce.core.web.catalog.ProductHandlerMapping;
import org.broadleafcommerce.inventory.domain.Inventory;
import org.broadleafcommerce.inventory.service.InventoryService;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.hibernate.tool.hbm2x.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.ssbusy.core.account.domain.MyCustomer;
import com.ssbusy.core.like.service.LikeService;
import com.ssbusy.core.product.domain.MyProduct;

public class SsbProductController extends BroadleafAbstractController implements
		Controller {

	protected String defaultProductView = "catalog/product";
	protected static String MODEL_ATTRIBUTE_NAME = "product";

	@Resource(name = "ssbLikeService")
	protected LikeService likeService;

	@Resource(name = "blInventoryService")
	protected InventoryService inventoryService;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView model = new ModelAndView();
		Product product = (Product) request
				.getAttribute(ProductHandlerMapping.CURRENT_PRODUCT_ATTRIBUTE_NAME);
		if (product == null)
			return homeView(model);

		List<Sku> skus;
		MyCustomer customer = (MyCustomer) CustomerState.getCustomer();
		if (customer == null || customer.getRegion() == null) {
			skus = product.getSkus();
		} else {
			List<Inventory> invents = inventoryService.listAllInventories(product,
					customer.getRegion().getFulfillmentLocations());
			skus = new ArrayList<Sku>(invents.size());
			Map<Sku, Inventory> invs = new HashMap<Sku, Inventory>(invents.size());
			for (Inventory inv : invents) {
				skus.add(inv.getSku());
				invs.put(inv.getSku(), inv);
			}
			if (!skus.contains(product.getDefaultSku()))
				return homeView(model); // 分仓不卖defaultSku，则认为整个都没卖

			model.addObject("inventories", invs);
			skus.remove(product.getDefaultSku());
		}
		model.addObject("skus", skus);

		MyProduct myproduct = (MyProduct) product;
		if (myproduct.getTotalSaled() == null) {
			myproduct.setTotalSaled(0L);
		}
		if (myproduct.getTotalLike() == null) {
			myproduct.setTotalLike(0L);
		}
		model.addObject(MODEL_ATTRIBUTE_NAME, product);

		if (StringUtils.isNotEmpty(product.getDisplayTemplate())) {
			model.setViewName(product.getDisplayTemplate());
		} else {
			model.setViewName(getDefaultProductView());
		}
		return model;
	}

	private ModelAndView homeView(ModelAndView model) {
		model.setViewName("redirect:/");
		return model;
	}

	public String getDefaultProductView() {
		return defaultProductView;
	}

	public void setDefaultProductView(String defaultProductView) {
		this.defaultProductView = defaultProductView;
	}

}
