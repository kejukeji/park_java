package com.ssbusy.site.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.broadleafcommerce.core.catalog.domain.Category;
import org.broadleafcommerce.core.catalog.domain.CategoryAttribute;
import org.broadleafcommerce.core.catalog.domain.CategoryXref;
import org.broadleafcommerce.core.web.processor.CategoriesProcessor;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Attribute;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.NestableNode;
import org.thymeleaf.dom.Node;

import com.ssbusy.core.account.domain.MyCustomer;

/**
 * 同時將result放到parent的local variables
 * 
 * @author Ju
 */
public class MyCategoriesProcessor extends CategoriesProcessor {

	@Override
	protected void modifyModelAttributes(Arguments arguments, Element element) {
		String resultVar = element.getAttributeValue("resultVar");
		String parentCategory = element.getAttributeValue("parentCategory");
		String unparsedMaxResults = element.getAttributeValue("maxResults");
		// 分仓过滤遍历的子分类深度
		String iterateLevelStr = element.getAttributeValue("iterateLevel");

		// TODO: Potentially write an algorithm that will pick the minimum depth
		// category
		// instead of the first category in the list
		List<Category> categories = catalogService
				.findCategoriesByName(parentCategory);
		if (categories != null && categories.size() > 0) {
			// gets child categories in order ONLY if they are in the xref table
			// and active
			List<CategoryXref> subcategories = categories.get(0)
					.getChildCategoryXrefs();
			if (subcategories != null && !subcategories.isEmpty()) {
				if (StringUtils.isNotEmpty(unparsedMaxResults)) {
					int maxResults = Integer.parseInt(unparsedMaxResults);
					if (subcategories.size() > maxResults) {
						subcategories = subcategories.subList(0, maxResults);
					}
				}
			}

			int iterateLevel = 1;
			if (iterateLevelStr != null && !iterateLevelStr.isEmpty()) {
				iterateLevel = Integer.parseInt(iterateLevelStr);
			}

			MyCustomer customer = (MyCustomer) CustomerState.getCustomer();
			Long regionId = customer == null || customer.getRegion() == null ? null
					: customer.getRegion().getId();
			subcategories = filterCatsByRegion(subcategories,
					regionId == null ? null : "," + regionId + ",",
					iterateLevel);

			List<Category> results = new ArrayList<Category>(
					subcategories.size());
			for (CategoryXref xref : subcategories) {
				results.add(xref.getSubCategory());
			}
			addToModel(arguments, resultVar, results);

			String localKey = element.getAttributeValue("localKey");
			if (localKey == null)
				return;
			NestableNode parent = element.getParent();
			if (parent == null || parent.getChildren() == null)
				return;
			for (Node child : parent.getChildren()) {
				if (child == element || !(child instanceof Element))
					continue;
				Map<String, Attribute> attrs = ((Element) child)
						.getAttributeMap();
				if (attrs == null || attrs.isEmpty())
					continue;
				for (Attribute attr : attrs.values()) {
					if (attr.getValue() != null
							&& attr.getValue().indexOf(localKey) >= 0) {
						child.setNodeLocalVariable(localKey, results);
						return;
					}
				}
			}
		}
	}

	private List<CategoryXref> filterCatsByRegion(List<CategoryXref> cats,
			String regionId, int iterateLevel) {
		if (regionId == null || iterateLevel <= 0 || cats == null)
			return cats;
		iterateLevel--;
		List<CategoryXref> ret = new ArrayList<CategoryXref>(cats.size());
		for (CategoryXref xref : cats) {
			Category subCat = xref.getSubCategory();
			// 某些分仓排除该分类
			CategoryAttribute attr = subCat
					.getCategoryAttributeByName("region-excluded-ids");
			if (attr != null) {
				String regionIds = "," + attr.getValue() + ",";
				if (regionIds.indexOf(regionId) >= 0)
					continue;
			}
			ret.add(xref);
			// 子类排除
			if (iterateLevel > 0) {
				List<CategoryXref> subCats = filterCatsByRegion(
						subCat.getChildCategoryXrefs(), regionId, iterateLevel);
				subCat.setChildCategoryXrefs(subCats);
			}
		}
		return ret;
	}

}
