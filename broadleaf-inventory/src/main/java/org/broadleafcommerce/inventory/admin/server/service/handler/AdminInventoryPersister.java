/*
 * Copyright 2008-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.broadleafcommerce.inventory.admin.server.service.handler;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ConcurrentModificationException;
import java.util.Map;
   
import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.broadleafcommerce.inventory.dao.InventoryDao;
import org.broadleafcommerce.inventory.domain.Inventory;
import org.broadleafcommerce.inventory.exception.ConcurrentInventoryModificationException;
import org.broadleafcommerce.inventory.exception.InventoryUnavailableException;
import org.broadleafcommerce.inventory.service.InventoryService;
import org.broadleafcommerce.openadmin.dto.Entity;
import org.broadleafcommerce.openadmin.dto.FieldMetadata;
import org.broadleafcommerce.openadmin.dto.Property;
import org.broadleafcommerce.openadmin.server.service.ValidationException;
import org.broadleafcommerce.openadmin.server.service.persistence.module.RecordHelper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Used in order to create a new transaction when trying to save inventory. Needed to create separate Spring bean component
 * for this logic because in order to be apart of a different transaction, the new transactional method has to be apart of
 * a different bean.
 * 
 * This component should only be used by the {@link InventoryCustomPersistenceHandler#update()} method.
 * 
 * @author Phillip Verheyden (phillipuniverse)
 *
 */
public class AdminInventoryPersister {

    @Resource(name = "blInventoryDao")
    protected InventoryDao inventoryDao;

    @Resource(name = "blInventoryService")
    protected InventoryService inventoryService;

    /**
     * Creates a new transaction and attempts a read, populate, update within the transaction. Retry logic should only
     * look for a {@link ConcurrentModificationException}. All other exceptions are thrown as a result of attempting to
     * populate the {@link Inventory} object from the <b>adminProperties</b> passed in.
     * 
     * @param inventory
     * @param entity
     * @param adminProperties
     * @param helper
     * @throws ConcurrentInventoryModificationException
     * @throws ClassNotFoundException 
     * @throws InstantiationException 
     * @throws ParseException 
     * @throws NoSuchMethodException 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @throws NumberFormatException 
     * @throws ValidationException 
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, value = "blTransactionManager", rollbackFor = { InventoryUnavailableException.class, ConcurrentInventoryModificationException.class })
    public Inventory saveAdminInventory(Long inventoryId, Entity entity, Map<String, FieldMetadata> adminProperties, RecordHelper helper)
            throws ConcurrentInventoryModificationException,
            NumberFormatException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParseException, InstantiationException, ClassNotFoundException, ValidationException {
        Inventory inventory = inventoryDao.readForUpdateById(inventoryId);
        inventory = populateInventory(inventory, entity, adminProperties, helper);
        //validation failure, return immediately
        if (inventory == null) {
            return null;
        }
        return inventoryService.save(inventory);
    }

    /**
     * Attempts to populate the given Inventory instance with the merged properties from the admin. Returns null if the
     * given <b>adminInstance</b> fails validation. If validation fails, invokers should return the entity that was passed
     * in since this will contain the validation failures.
     * 
     * @param adminInstance
     * @param entity
     * @param inventoryProperties
     * @param helper
     * @return
     * @throws NumberFormatException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws ParseException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws ValidationException 
     */
    protected Inventory populateInventory(Inventory adminInstance, Entity entity, Map<String, FieldMetadata> inventoryProperties, RecordHelper helper)
            throws NumberFormatException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParseException, InstantiationException, ClassNotFoundException, ValidationException {

        adminInstance = (Inventory) helper.createPopulatedInstance(adminInstance, entity, inventoryProperties, false);

        Integer quantityAvailableChange = 0;
        Integer quantityAvailableOnHandChange = 0;

        Property[] properties = entity.getProperties();
        for (Property property : properties) {
            if (InventoryCustomPersistenceHandler.QUANTITY_AVAILABLE_CHANGE_FIELD_NAME.equals(property.getName())) {
                quantityAvailableChange = NumberUtils.toInt(property.getValue());
            } else if (InventoryCustomPersistenceHandler.QUANTITY_ON_HAND_CHANGE_FIELD_NAME.equals(property.getName())) {
                quantityAvailableOnHandChange = NumberUtils.toInt(property.getValue());
            }
        }

        adminInstance.setQuantityAvailable(adminInstance.getQuantityAvailable() + quantityAvailableChange);
        adminInstance.setQuantityOnHand(adminInstance.getQuantityOnHand() + quantityAvailableOnHandChange);

        if (adminInstance.getQuantityAvailable() < 0) {
            entity.setValidationFailure(true);
            entity.addValidationError(InventoryCustomPersistenceHandler.QUANTITY_AVAILABLE_CHANGE_FIELD_NAME, "quantityAvailableIsNegative");
            return null;
        } else if (adminInstance.getQuantityOnHand() < 0) {
            entity.setValidationFailure(true);
            entity.addValidationError(InventoryCustomPersistenceHandler.QUANTITY_ON_HAND_CHANGE_FIELD_NAME, "quantityOnHandIsNegative");
            return null;
        }

        //avoid lazy initialization exceptions; initialize the ProductOptionValues for this Inventory's Sku
        adminInstance.getSku().getProductOptionValues().size();

        return adminInstance;
    }

}
