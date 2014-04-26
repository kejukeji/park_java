package org.broadleafcommerce.openadmin.server.service.persistence.module;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.core.catalog.domain.LocationAccessor;
import org.broadleafcommerce.inventory.domain.FulfillmentLocation;
import org.broadleafcommerce.openadmin.dto.CriteriaTransferObject;
import org.broadleafcommerce.openadmin.dto.Entity;
import org.broadleafcommerce.openadmin.dto.FieldMetadata;
import org.broadleafcommerce.openadmin.dto.PersistencePerspective;
import org.broadleafcommerce.openadmin.dto.SortDirection;
import org.broadleafcommerce.openadmin.server.security.domain.AdminUser;
import org.broadleafcommerce.openadmin.server.security.remote.SecurityVerifier;
import org.broadleafcommerce.openadmin.server.service.ValidationException;
import org.broadleafcommerce.openadmin.server.service.persistence.module.criteria.FieldPath;
import org.broadleafcommerce.openadmin.server.service.persistence.module.criteria.FieldPathBuilder;
import org.broadleafcommerce.openadmin.server.service.persistence.module.criteria.FilterMapping;
import org.broadleafcommerce.openadmin.server.service.persistence.module.criteria.Restriction;
import org.broadleafcommerce.openadmin.server.service.persistence.module.criteria.RestrictionFactory;
import org.broadleafcommerce.openadmin.server.service.persistence.module.criteria.predicate.PredicateProvider;

import com.ssbusy.admin.user.domain.MyAdminUser;

public class BasicPersistenceModuleEx extends BasicPersistenceModule {
	@Resource(name = "blAdminSecurityRemoteService")
	protected SecurityVerifier securityVerifier;

	@Override
	public Serializable createPopulatedInstance(Serializable instance,
			Entity entity, Map<String, FieldMetadata> unfilteredProperties,
			Boolean setId) throws ValidationException {
		instance = super.createPopulatedInstance(instance, entity,
				unfilteredProperties, setId);
		if (instance instanceof LocationAccessor) {
			AdminUser adminUser = securityVerifier.getPersistentAdminUser();
			if (adminUser instanceof MyAdminUser) {
				FulfillmentLocation location = ((MyAdminUser) adminUser)
						.getFulfillmentLocation();
				((LocationAccessor) instance).setLocation(location);
			}
		}
		return instance;
	}

	@Override
	public List<FilterMapping> getFilterMappings(
			PersistencePerspective persistencePerspective,
			CriteriaTransferObject cto, String clazzName,
			Map<String, FieldMetadata> mergedUnfilteredProperties,
			RestrictionFactory customRestrictionFactory) {
		List<FilterMapping> mappings = super.getFilterMappings(
				persistencePerspective, cto, clazzName,
				mergedUnfilteredProperties, customRestrictionFactory);
		// default id descending
		if (mergedUnfilteredProperties.get("id") != null)
			mappings.add(new FilterMapping().withRestriction(new Restriction())
					.withFullPropertyName("id")
					.withSortDirection(SortDirection.DESCENDING));
		Class<?> clazz = null;
		try {
			clazz = getLocationBasedClass(clazzName);
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		}
		if (clazz != null) {
			AdminUser adminUser = securityVerifier.getPersistentAdminUser();
			if (adminUser instanceof MyAdminUser) {
				FilterMapping filterMapping2 = new FilterMapping()
						.withFieldPath(
								new FieldPath()
										.withTargetProperty("location.id"))
						.withFilterValues(
								Arrays.asList(String
										.valueOf(((MyAdminUser) adminUser)
												.getFulfillmentLocation()
												.getId())))
						.withRestriction(
								new Restriction()
										.withPredicateProvider(new PredicateProvider<Serializable, String>() {
											@Override
											public Predicate buildPredicate(
													CriteriaBuilder builder,
													FieldPathBuilder fieldPathBuilder,
													From root,
													String ceilingEntity,
													String fullPropertyName,
													Path<Serializable> explicitPath,
													List<String> directValues) {
												if (String.class
														.isAssignableFrom(explicitPath
																.getJavaType())) {
													return builder
															.equal(explicitPath,
																	directValues
																			.get(0));
												} else {
													return builder.equal(
															explicitPath,
															Long.parseLong(directValues
																	.get(0)));
												}
											}
										}));
				mappings.add(filterMapping2);
			}
		}
		return mappings;
	}

	private Class<?> getLocationBasedClass(String clazz)
			throws ServiceException {
		try {
			Class<?>[] entities = persistenceManager
					.getAllPolymorphicEntitiesFromCeiling(Class.forName(clazz));
			for (int i = 0; i < entities.length; i++) {
				if (LocationAccessor.class.isAssignableFrom(entities[i])) {
					return entities[i];
				}
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException("Unable to fetch results for " + clazz,
					e);
		}
	}

}
