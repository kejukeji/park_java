package org.broadleafcommerce.inventory.admin.server.service.handler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.admin.server.service.handler.SkuCustomPersistenceHandler;
import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.common.presentation.client.SupportedFieldType;
import org.broadleafcommerce.common.presentation.client.VisibilityEnum;
import org.broadleafcommerce.inventory.domain.Inventory;
import org.broadleafcommerce.inventory.domain.InventoryImpl;
import org.broadleafcommerce.inventory.service.InventoryService;
import org.broadleafcommerce.openadmin.dto.BasicFieldMetadata;
import org.broadleafcommerce.openadmin.dto.ClassMetadata;
import org.broadleafcommerce.openadmin.dto.DynamicResultSet;
import org.broadleafcommerce.openadmin.dto.Entity;
import org.broadleafcommerce.openadmin.dto.FieldMetadata;
import org.broadleafcommerce.openadmin.dto.MergedPropertyType;
import org.broadleafcommerce.openadmin.dto.PersistencePackage;
import org.broadleafcommerce.openadmin.dto.PersistencePerspective;
import org.broadleafcommerce.openadmin.server.dao.DynamicEntityDao;
import org.broadleafcommerce.openadmin.server.security.remote.SecurityVerifier;
import org.broadleafcommerce.openadmin.server.service.handler.CustomPersistenceHandlerAdapter;
import org.broadleafcommerce.openadmin.server.service.persistence.module.InspectHelper;
import org.broadleafcommerce.openadmin.server.service.persistence.module.RecordHelper;

import com.ssbusy.core.invoicing.domain.Invoicing;
import com.ssbusy.core.invoicing.domain.InvoicingImpl;

public class InvoicingCustomPersistenceHandler extends
		CustomPersistenceHandlerAdapter {

	private static final Log LOG = LogFactory
			.getLog(InvoicingCustomPersistenceHandler.class);

	@Resource(name = "blInventoryService")
	private InventoryService inventoryService;

	@Resource(name = "blAdminSecurityRemoteService")
	protected SecurityVerifier securityVerifier;

	@Override
	public Boolean canHandleUpdate(PersistencePackage persistencePackage) {
		String className = persistencePackage
				.getCeilingEntityFullyQualifiedClassname();
		return Invoicing.class.getName().equals(className)
				|| InvoicingImpl.class.getName().equals(className);
	}

	@Override
	public Boolean canHandleInspect(PersistencePackage persistencePackage) {
		return false;
	}

	@Override
	public Boolean canHandleFetch(PersistencePackage persistencePackage) {
		return false;
	}

	@Override
	public Boolean canHandleAdd(PersistencePackage persistencePackage) {
		return canHandleUpdate(persistencePackage);
	}

	@Override
	public Entity add(PersistencePackage persistencePackage,
			DynamicEntityDao dynamicEntityDao, RecordHelper helper)
			throws ServiceException {
		Entity entity = persistencePackage.getEntity();
		try {
			PersistencePerspective persistencePerspective = persistencePackage
					.getPersistencePerspective();
			Invoicing adminInstance = (Invoicing) Class.forName(
					entity.getType()[0]).newInstance();
			Map<String, FieldMetadata> adminProperties = helper
					.getSimpleMergedProperties(Invoicing.class.getName(),
							persistencePerspective);
			adminInstance = (Invoicing) helper.createPopulatedInstance(
					adminInstance, entity, adminProperties, false);
			if (adminInstance.getPurchaseDate() == null) {
				adminInstance.setPurchaseDate(new Date());
			}
			if (adminInstance.getInventory() != null
					&& adminInstance != null
					&& adminInstance.getInventory().getSku().getRetailPrice() != null) {
				adminInstance.setRetailPrice(adminInstance.getInventory()
						.getSku().getRetailPrice().getAmount());
				adminInstance.setMaolli(adminInstance.getInventory()
						.getSku().getRetailPrice().getAmount().subtract(adminInstance.getPurchasePrice()));
			}
			if (adminInstance.getInventory() != null
					&& adminInstance != null
					&& adminInstance.getInventory().getSku().getSalePrice() != null) {
				adminInstance.setSalePrice(adminInstance.getInventory()
						.getSku().getSalePrice().getAmount());
				adminInstance.setMaolli(adminInstance.getInventory()
						.getSku().getSalePrice().getAmount().subtract(adminInstance.getPurchasePrice()));
			}
			adminInstance = (Invoicing) dynamicEntityDao.merge(adminInstance);
			inventoryService.incrementInventory(adminInstance.getInventory(),
					adminInstance.getPurchaseQuantity());
			return helper.getRecord(adminProperties, adminInstance, null, null);
		} catch (Exception e) {
			LOG.error("Unable to add entity for " + entity.getType()[0], e);
			throw new ServiceException("Unable to add entity for "
					+ entity.getType()[0], e);
		}
	}

	@Override
	public DynamicResultSet inspect(PersistencePackage persistencePackage,
			DynamicEntityDao dynamicEntityDao, InspectHelper helper)
			throws ServiceException {
		try {

			PersistencePerspective persistencePerspective = persistencePackage
					.getPersistencePerspective();
			Map<MergedPropertyType, Map<String, FieldMetadata>> allMergedProperties = new HashMap<MergedPropertyType, Map<String, FieldMetadata>>();

			// retrieve the default properties for Inventory
			Map<String, FieldMetadata> properties = helper
					.getSimpleMergedProperties(Invoicing.class.getName(),
							persistencePerspective);

			// create a new field to hold change in quantity available
			BasicFieldMetadata quantityAvailableChangeMetadata = new BasicFieldMetadata();
			quantityAvailableChangeMetadata
					.setFieldType(SupportedFieldType.INTEGER);
			quantityAvailableChangeMetadata.setMutable(true);
			quantityAvailableChangeMetadata
					.setInheritedFromType(InventoryImpl.class.getName());
			quantityAvailableChangeMetadata
					.setAvailableToTypes(new String[] { InventoryImpl.class
							.getName() });
			quantityAvailableChangeMetadata.setForeignKeyCollection(false);
			quantityAvailableChangeMetadata
					.setMergedPropertyType(MergedPropertyType.PRIMARY);
			// quantityAvailableChangeMetadata
			// .setName(QUANTITY_AVAILABLE_CHANGE_FIELD_NAME);
			quantityAvailableChangeMetadata
					.setFriendlyName("quantityAvailableChange");
			quantityAvailableChangeMetadata
					.setHelpText("quantityAvailableChangeHelp");
			quantityAvailableChangeMetadata.setGroup("Quantities");
			quantityAvailableChangeMetadata.setOrder(6);
			quantityAvailableChangeMetadata
					.setExplicitFieldType(SupportedFieldType.INTEGER);
			quantityAvailableChangeMetadata.setProminent(false);
			quantityAvailableChangeMetadata.setBroadleafEnumeration("");
			quantityAvailableChangeMetadata.setReadOnly(false);
			quantityAvailableChangeMetadata
					.setVisibility(VisibilityEnum.FORM_EXPLICITLY_SHOWN);
			quantityAvailableChangeMetadata.setExcluded(false);

			// properties.put(QUANTITY_AVAILABLE_CHANGE_FIELD_NAME,
			// quantityAvailableChangeMetadata);

			// create a new field to hold change in quantity available
			BasicFieldMetadata quantityOnHandChangeMetadata = new BasicFieldMetadata();
			quantityOnHandChangeMetadata
					.setFieldType(SupportedFieldType.INTEGER);
			quantityOnHandChangeMetadata.setMutable(true);
			quantityOnHandChangeMetadata
					.setInheritedFromType(InventoryImpl.class.getName());
			quantityOnHandChangeMetadata
					.setAvailableToTypes(new String[] { InventoryImpl.class
							.getName() });
			quantityOnHandChangeMetadata.setForeignKeyCollection(false);
			quantityOnHandChangeMetadata
					.setMergedPropertyType(MergedPropertyType.PRIMARY);
			// quantityOnHandChangeMetadata
			// .setName(QUANTITY_ON_HAND_CHANGE_FIELD_NAME);
			quantityOnHandChangeMetadata
					.setFriendlyName("quantityOnHandChange");
			quantityOnHandChangeMetadata
					.setHelpText("quantityOnHandChangeHelp");
			quantityOnHandChangeMetadata.setGroup("Quantities");
			quantityOnHandChangeMetadata.setOrder(7);
			quantityOnHandChangeMetadata
					.setExplicitFieldType(SupportedFieldType.INTEGER);
			quantityOnHandChangeMetadata.setProminent(false);
			quantityOnHandChangeMetadata.setBroadleafEnumeration("");
			quantityOnHandChangeMetadata.setReadOnly(false);
			quantityOnHandChangeMetadata
					.setVisibility(VisibilityEnum.FORM_EXPLICITLY_SHOWN);
			quantityOnHandChangeMetadata.setExcluded(false);

			// properties.put(QUANTITY_ON_HAND_CHANGE_FIELD_NAME,
			// quantityOnHandChangeMetadata);

			// add in the consolidated product options field
			FieldMetadata options = SkuCustomPersistenceHandler
					.createConsolidatedOptionField(InventoryImpl.class);
			options.setOrder(3);
			properties
					.put(SkuCustomPersistenceHandler.CONSOLIDATED_PRODUCT_OPTIONS_FIELD_NAME,
							options);

			allMergedProperties.put(MergedPropertyType.PRIMARY, properties);
			Class<?>[] entityClasses = dynamicEntityDao
					.getAllPolymorphicEntitiesFromCeiling(Inventory.class);
			ClassMetadata mergedMetadata = helper.getMergedClassMetadata(
					entityClasses, allMergedProperties);

			return new DynamicResultSet(mergedMetadata, null, null);

		} catch (Exception e) {
			String className = persistencePackage
					.getCeilingEntityFullyQualifiedClassname();
			ServiceException ex = new ServiceException(
					"Unable to retrieve inspection results for " + className, e);
			LOG.error("Unable to retrieve inspection results for " + className,
					ex);
			throw ex;
		}
	}

}
