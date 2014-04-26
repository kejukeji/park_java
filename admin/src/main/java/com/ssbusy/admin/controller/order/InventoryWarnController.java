package com.ssbusy.admin.controller.order;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.broadleafcommerce.inventory.domain.Inventory;
import org.broadleafcommerce.inventory.service.InventoryService;
import org.broadleafcommerce.openadmin.server.security.remote.SecurityVerifier;
import org.broadleafcommerce.openadmin.server.security.service.AdminSecurityService;
import org.broadleafcommerce.openadmin.web.controller.AdminAbstractController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssbusy.admin.user.domain.MyAdminUser;

@Controller
public class InventoryWarnController extends AdminAbstractController {

	@Resource(name = "blInventoryService")
	protected InventoryService inventoryService;
	@Resource(name = "blAdminSecurityService")
	private AdminSecurityService securityService;
	@Resource(name = "blAdminSecurityRemoteService")
	protected SecurityVerifier securityVerifier;
	@Value("${inventory_threshold}")
	private int inventoryThreshold;

	private static String SHOW_INVENTORY = "inventory/inventory";

	@ResponseBody
	@RequestMapping("inventory-warn")
	protected List<SkuOfInventoryForm> InventoryWarn(HttpServletRequest request) {
		MyAdminUser myAdminUser = (MyAdminUser) securityVerifier
				.getPersistentAdminUser();
		List<Inventory> inventoryes = inventoryService
				.readInventoryForLessThanQuantity(inventoryThreshold,
						myAdminUser.getFulfillmentLocation().getId());
		List<SkuOfInventoryForm> skuForms = new ArrayList<SkuOfInventoryForm>();
		for (int i = 0; i < inventoryes.size(); i++) {
			SkuOfInventoryForm skuForm = new SkuOfInventoryForm();
			skuForm.setId(inventoryes.get(i).getSku().getId());
			skuForm.setName(inventoryes.get(i).getSku().getName());
			skuForm.setQuantity(inventoryes.get(i).getQuantityAvailable());
			skuForms.add(skuForm);
		}
		return skuForms;
	}

	@RequestMapping("xiao-inventory")
	public String showInventory(Model model) {
		super.setModelAttributes(model, "xiao-inventory");
		MyAdminUser myAdminUser = (MyAdminUser) securityVerifier
				.getPersistentAdminUser();
		model.addAttribute("inventorys", inventoryService
				.readInventoryForFulfillmentLocation(myAdminUser
						.getFulfillmentLocation()));
		return SHOW_INVENTORY;
	}
}
