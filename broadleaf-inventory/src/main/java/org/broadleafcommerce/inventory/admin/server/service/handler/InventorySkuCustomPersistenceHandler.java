/**
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.broadleafcommerce.inventory.admin.server.service.handler;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.admin.server.service.handler.SkuCustomPersistenceHandler;
import org.broadleafcommerce.core.catalog.domain.SkuImpl;
import org.broadleafcommerce.core.catalog.service.CatalogService;
import org.broadleafcommerce.inventory.domain.InventoryImpl;
import org.broadleafcommerce.inventory.service.InventoryService;
import org.broadleafcommerce.openadmin.dto.CriteriaTransferObject;
import org.broadleafcommerce.openadmin.dto.PersistencePackage;
import org.broadleafcommerce.openadmin.server.service.handler.CustomPersistenceHandler;
import org.broadleafcommerce.openadmin.server.service.persistence.module.criteria.FieldPath;
import org.broadleafcommerce.openadmin.server.service.persistence.module.criteria.FieldPathBuilder;
import org.broadleafcommerce.openadmin.server.service.persistence.module.criteria.FilterMapping;
import org.broadleafcommerce.openadmin.server.service.persistence.module.criteria.Restriction;
import org.broadleafcommerce.openadmin.server.service.persistence.module.criteria.predicate.PredicateProvider;

public class InventorySkuCustomPersistenceHandler extends
		SkuCustomPersistenceHandler {

	private static final Log LOG = LogFactory
			.getLog(InventorySkuCustomPersistenceHandler.class);

	@Resource(name = "blInventoryService")
	protected InventoryService inventoryService;

	@Resource(name = "blCatalogService")
	protected CatalogService catalogService;

	@Override
	public Boolean canHandleInspect(PersistencePackage persistencePackage) {
		String className = persistencePackage
				.getCeilingEntityFullyQualifiedClassname();
		String[] customCriteria = persistencePackage.getCustomCriteria();
		return customCriteria != null && customCriteria.length > 0
				&& SkuImpl.class.getName().equals(className)
				&& "inventoryFilteredSkuList".equals(customCriteria[0]);
	}

	@Override
	public Boolean canHandleFetch(PersistencePackage persistencePackage) {
		return canHandleInspect(persistencePackage);
	}

	@Override
	public void applyAdditionalFetchCriteria(
			List<FilterMapping> filterMappings, CriteriaTransferObject cto,
			PersistencePackage persistencePackage) {
		super.applyAdditionalFetchCriteria(filterMappings, cto,
				persistencePackage);
		// grab the fulfillment location off of the custom criteria from the
		// frontend
		final Long locationId = Long.parseLong(persistencePackage
				.getCustomCriteria()[1]);
		FilterMapping f = new FilterMapping().withFieldPath(
				new FieldPath().withTargetProperty("id")).withRestriction(
				new Restriction()
						.withPredicateProvider(new PredicateProvider() {
							@Override
							public Predicate buildPredicate(
									CriteriaBuilder builder,
									FieldPathBuilder fieldPathBuilder,
									From root, String ceilingEntity,
									String fullPropertyName, Path explicitPath,
									List directValues) {
								// DetachedCriteria locationSkuIds1 =
								// DetachedCriteria.forClass(InventoryImpl.class)
								// .add(Restrictions.eq("fulfillmentLocation.id",
								// locationId))
								// .setProjection(Projections.property("sku.id"));
								// return
								// Subqueries.propertyNotIn(targetPropertyName,
								// locationSkuIds);

								CriteriaQuery<Long> q = builder
										.createQuery(Long.class);
								Root<InventoryImpl> subRoot = q
										.from(InventoryImpl.class);
								q.where(builder.equal(
										subRoot.get("fulfillmentLocation.id"),
										locationId));
								q.select(subRoot.<Long> get("sku.id"));
								return builder.not(explicitPath.in(q)); // FIXME
							}
						}));
		filterMappings.add(f);
	}

	@Override
	public int getOrder() {
		return CustomPersistenceHandler.DEFAULT_ORDER - 100;
	}

}
