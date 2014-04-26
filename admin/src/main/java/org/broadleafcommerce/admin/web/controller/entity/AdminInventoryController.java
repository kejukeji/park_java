package org.broadleafcommerce.admin.web.controller.entity;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.broadleafcommerce.openadmin.web.controller.entity.AdminBasicEntityController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("blAdminInventoryController")
@RequestMapping("/" + AdminInventoryController.SECTION_KEY)
public class AdminInventoryController extends AdminBasicEntityController {

	protected static final String SECTION_KEY = "inventory";

	@Override
	protected String getSectionKey(Map<String, String> pathVars) {
		if (super.getSectionKey(pathVars) != null) {
			return super.getSectionKey(pathVars);
		}
		return SECTION_KEY;
	}

	@Override
	public String[] getSectionCustomCriteria() {
		return new String[] { "inventoryUpdate" };
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String viewEntityList(HttpServletRequest request,
			HttpServletResponse response, Model model,
			@PathVariable Map<String, String> pathVars,
			@RequestParam MultiValueMap<String, String> requestParams)
			throws Exception {
		String ret = super.viewEntityList(request, response, model, pathVars,
				requestParams);

		// 上下架操作
//		ListGrid listGrid = (ListGrid) model.asMap().get("listGrid");
//		ListGridAction upDownJia = new ListGridAction("UP_DOWN_JIA")
//				.withDisplayText("Generate_Skus")
//				.withIconClass("icon-fighter-jet")
//				.withButtonClass("generate-skus")
//				.withUrlPostfix("/generate-skus")
//				.withForListGridReadOnly(listGrid.getReadOnly());
//		listGrid.addRowAction(upDownJia);
//		listGrid.setSubCollectionFieldName("NA");
//		listGrid.setListGridType(Type.BASIC);

		return ret;
	}
}
