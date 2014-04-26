package org.broadleafcommerce.openadmin.server.service.persistence.module;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.common.presentation.client.PersistencePerspectiveItemType;
import org.broadleafcommerce.core.catalog.domain.LocationAccessor;
import org.broadleafcommerce.inventory.domain.FulfillmentLocation;
import org.broadleafcommerce.openadmin.dto.AdornedTargetList;
import org.broadleafcommerce.openadmin.dto.CriteriaTransferObject;
import org.broadleafcommerce.openadmin.dto.DynamicResultSet;
import org.broadleafcommerce.openadmin.dto.Entity;
import org.broadleafcommerce.openadmin.dto.FieldMetadata;
import org.broadleafcommerce.openadmin.dto.PersistencePackage;
import org.broadleafcommerce.openadmin.dto.PersistencePerspective;
import org.broadleafcommerce.openadmin.server.security.domain.AdminUser;
import org.broadleafcommerce.openadmin.server.security.remote.SecurityVerifier;
import org.broadleafcommerce.openadmin.server.service.persistence.module.criteria.FieldPath;
import org.broadleafcommerce.openadmin.server.service.persistence.module.criteria.FieldPathBuilder;
import org.broadleafcommerce.openadmin.server.service.persistence.module.criteria.FilterMapping;
import org.broadleafcommerce.openadmin.server.service.persistence.module.criteria.Restriction;
import org.broadleafcommerce.openadmin.server.service.persistence.module.criteria.predicate.PredicateProvider;

import com.ssbusy.admin.user.domain.MyAdminUser;

public class AdornedTargetListPersistenceModuleEx extends
		AdornedTargetListPersistenceModule {
	@Resource(name = "blAdminSecurityRemoteService")
	protected SecurityVerifier securityVerifier;

	@Override
	public Entity add(PersistencePackage persistencePackage)
			throws ServiceException {
		AdornedTargetList adornedTargetList = (AdornedTargetList) persistencePackage
				.getPersistencePerspective().getPersistencePerspectiveItems()
				.get(PersistencePerspectiveItemType.ADORNEDTARGETLIST);
		Class<?> clazz = getLocationBasedClass(adornedTargetList);
		if (clazz != null) {
			AdminUser adminUser = securityVerifier.getPersistentAdminUser();
			if (adminUser instanceof MyAdminUser) {
				adornedTargetList.setAdornedTargetEntityClassname(clazz
						.getName());
			}
		}
		return super.add(persistencePackage);
	}

	@Override
	protected Serializable createPopulatedAdornedTargetInstance(
			AdornedTargetList adornedTargetList, Entity entity)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, NumberFormatException,
			InvocationTargetException, NoSuchMethodException,
			FieldNotAvailableException {
		Serializable instance = super.createPopulatedAdornedTargetInstance(
				adornedTargetList, entity);
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
	public DynamicResultSet fetch(PersistencePackage persistencePackage,
			CriteriaTransferObject cto) throws ServiceException {
		AdornedTargetList adornedTargetList = (AdornedTargetList) persistencePackage
				.getPersistencePerspective().getPersistencePerspectiveItems()
				.get(PersistencePerspectiveItemType.ADORNEDTARGETLIST);
		Class<?> clazz = getLocationBasedClass(adornedTargetList);
		if (clazz != null) {
			AdminUser adminUser = securityVerifier.getPersistentAdminUser();
			if (adminUser instanceof MyAdminUser) {
				FulfillmentLocation location = ((MyAdminUser) adminUser)
						.getFulfillmentLocation();
				// location filter
				cto.get("location.id").setFilterValue(
						String.valueOf(location.getId()));
				adornedTargetList.setAdornedTargetEntityClassname(clazz
						.getName());
			}

		}

		return super.fetch(persistencePackage, cto);
	}

	private Class<?> getLocationBasedClass(AdornedTargetList adornedTargetList)
			throws ServiceException {
		try {
			Class<?>[] entities = persistenceManager
					.getAllPolymorphicEntitiesFromCeiling(Class
							.forName(adornedTargetList
									.getAdornedTargetEntityClassname()));
			for (int i = 0; i < entities.length; i++) {
				if (LocationAccessor.class.isAssignableFrom(entities[i])) {
					adornedTargetList
							.setAdornedTargetEntityClassname(entities[i]
									.getName());
					return entities[i];
				}
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException("Unable to fetch results for "
					+ adornedTargetList, e);
		}
	}

	@Override
	public List<FilterMapping> getAdornedTargetFilterMappings(
			PersistencePerspective persistencePerspective,
			CriteriaTransferObject cto,
			Map<String, FieldMetadata> mergedProperties,
			AdornedTargetList adornedTargetList) throws ClassNotFoundException {
		List<FilterMapping> mappings = super.getAdornedTargetFilterMappings(
				persistencePerspective, cto, mergedProperties,
				adornedTargetList);
		Class<?> clazz = null;
		try {
			clazz = getLocationBasedClass(adornedTargetList);
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
}
