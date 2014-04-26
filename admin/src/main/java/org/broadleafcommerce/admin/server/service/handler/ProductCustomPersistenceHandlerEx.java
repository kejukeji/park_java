package org.broadleafcommerce.admin.server.service.handler;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.inventory.domain.FulfillmentLocation;
import org.broadleafcommerce.inventory.domain.Inventory;
import org.broadleafcommerce.openadmin.dto.CriteriaTransferObject;
import org.broadleafcommerce.openadmin.dto.DynamicResultSet;
import org.broadleafcommerce.openadmin.dto.Entity;
import org.broadleafcommerce.openadmin.dto.FieldMetadata;
import org.broadleafcommerce.openadmin.dto.PersistencePackage;
import org.broadleafcommerce.openadmin.server.dao.DynamicEntityDao;
import org.broadleafcommerce.openadmin.server.security.domain.AdminUser;
import org.broadleafcommerce.openadmin.server.security.remote.SecurityVerifier;
import org.broadleafcommerce.openadmin.server.service.persistence.module.RecordHelper;
import org.broadleafcommerce.openadmin.server.service.persistence.module.criteria.CriteriaTranslatorEx;
import org.broadleafcommerce.openadmin.server.service.persistence.module.criteria.FilterMapping;

import com.ssbusy.admin.user.domain.MyAdminUser;

public class ProductCustomPersistenceHandlerEx extends
		ProductCustomPersistenceHandler {

	@Resource(name = "blAdminSecurityRemoteService")
	protected SecurityVerifier securityVerifier;

	@Resource(name = "blCriteriaTranslator")
	protected CriteriaTranslatorEx criteriaTranslator;

	@Override
	public Boolean canHandleFetch(PersistencePackage persistencePackage) {
		String clazz = persistencePackage
				.getCeilingEntityFullyQualifiedClassname();
		try {
			// TODO
			return persistencePackage.getPersistencePerspective()
					.getPersistencePerspectiveItems().isEmpty()
					&& Product.class.isAssignableFrom(Class.forName(clazz));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("", e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public DynamicResultSet fetch(PersistencePackage persistencePackage,
			CriteriaTransferObject cto, DynamicEntityDao dynamicEntityDao,
			RecordHelper helper) throws ServiceException {
		String ceilingClassname = persistencePackage
				.getCeilingEntityFullyQualifiedClassname();
		Map<String, FieldMetadata> originalProps = helper
				.getSimpleMergedProperties(ceilingClassname,
						persistencePackage.getPersistencePerspective());
		List<FilterMapping> filterMappings = helper.getFilterMappings(
				persistencePackage.getPersistencePerspective(), cto,
				ceilingClassname, originalProps);
		CriteriaQuery<Serializable> criteria = criteriaTranslator
				.translateCriteriaQuery(dynamicEntityDao, ceilingClassname,
						filterMappings);
		CriteriaBuilder criteriaBuilder = dynamicEntityDao
				.getStandardEntityManager().getCriteriaBuilder();
		Root<?> root = criteria.getRoots().iterator().next();

		AdminUser adminUser = securityVerifier.getPersistentAdminUser();
		if (adminUser instanceof MyAdminUser) {
			FulfillmentLocation location = ((MyAdminUser) adminUser)
					.getFulfillmentLocation();

			// inventory
			Class<?>[] polyEntities = dynamicEntityDao
					.getAllPolymorphicEntitiesFromCeiling(Inventory.class);
			Class<Serializable> ceilingInventoryClass = (Class<Serializable>) polyEntities[polyEntities.length - 1];
			Root<Serializable> from = criteria.from(ceilingInventoryClass);
			Predicate locFilter = criteriaBuilder.equal(
					from.get("fulfillmentLocation"), location);
			// join
			Predicate joinFilter = criteriaBuilder
					.equal(from.get("sku").get("id"), root.get("defaultSku")
							.get("id"));

			Predicate restriction = criteria.getRestriction();
			criteria.where(restriction, joinFilter, locFilter);
		}
		TypedQuery<Serializable> query = dynamicEntityDao
				.getStandardEntityManager().createQuery(criteria);
		if (cto.getFirstResult() != null)
			query.setFirstResult(cto.getFirstResult());
		if (cto.getMaxResults() != null)
			query.setMaxResults(cto.getMaxResults());

		List<Serializable> records = query.getResultList();
		criteria.select(criteriaBuilder.count(root));
		criteria.orderBy();
		query = dynamicEntityDao.getStandardEntityManager().createQuery(
				criteria);
		int totalRecords = ((Long) query.getSingleResult()).intValue();
		Entity[] payload = helper.getRecords(originalProps, records);
		return new DynamicResultSet(null, payload, totalRecords);
	}
}
