/**
 * sudaw copy right 1.0 
 */
package org.broadleafcommerce.core.search.service.solr;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.broadleafcommerce.common.locale.domain.Locale;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.extension.ExtensionResultStatusType;
import org.broadleafcommerce.core.search.domain.Field;
import org.broadleafcommerce.core.search.domain.ProductSearchCriteria;
import org.broadleafcommerce.core.search.domain.SearchFacetDTO;
import org.broadleafcommerce.core.search.domain.solr.FieldType;
import org.broadleafcommerce.inventory.domain.FulfillmentLocation;
import org.broadleafcommerce.inventory.domain.Inventory;
import org.broadleafcommerce.inventory.service.InventoryService;
import org.springframework.stereotype.Service;

/**
 * @author jamesp
 */
@Service("ssbInventorySolrSearchServiceExtensionHandler")
public class InventorySolrSearchServiceExtensionHandler extends
		AbstractSolrSearchServiceExtensionHandler {

	public static ThreadLocal<List<FulfillmentLocation>> customerLocation = new ThreadLocal<List<FulfillmentLocation>>();

	/**
	 * 库存建立索引
	 */
	public static String INVENTORY_MAP = "inventory";

	@Resource(name = "blSolrSearchServiceExtensionManager")
	protected SolrSearchServiceExtensionManager extensionManager;

	@Resource(name = "blInventoryService")
	protected InventoryService inventoryService;

	@PostConstruct
	public void init() {
		extensionManager.getHandlers().add(this);
	}

	/**
	 * 建分仓索引
	 */
	@Override
	public ExtensionResultStatusType addPropertyValues(Product product,
			Field field, FieldType fieldType, Map<String, Object> values,
			String propertyName, List<Locale> locales)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {

		if (propertyName.contains(INVENTORY_MAP)) {
			String propName = propertyName
					.substring(INVENTORY_MAP.length() + 1);
			// 默认sku的库存相关
			List<Inventory> inventories = inventoryService
					.listInventories(product.getDefaultSku());
			for (Inventory inv : inventories) {
				Object val = PropertyUtils.getProperty(inv, propName);
				values.put("loc" + val, "1");
			}
			return ExtensionResultStatusType.HANDLED;
		} else {
			return ExtensionResultStatusType.NOT_HANDLED;
		}
	}

	/**
	 * 添加分仓过滤
	 */
	@Override
	public ExtensionResultStatusType modifySolrQuery(SolrQuery query,
			String qualifiedSolrQuery, List<SearchFacetDTO> facets,
			ProductSearchCriteria searchCriteria, String defaultSort) {
		List<FulfillmentLocation> locations = customerLocation.get();
		if (locations == null || locations.isEmpty())
			return ExtensionResultStatusType.NOT_HANDLED;

		StringBuilder sb = new StringBuilder("(");
		for (FulfillmentLocation loc : locations) {
			sb.append("loc").append(loc.getId()).append("_invloc_l:1 OR ");
		}
		sb.setLength(sb.length() - 4);
		query.addFilterQuery(sb.append(")").toString());
		return ExtensionResultStatusType.HANDLED;
	}
}
