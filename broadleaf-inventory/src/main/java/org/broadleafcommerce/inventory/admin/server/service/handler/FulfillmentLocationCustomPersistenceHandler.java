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

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.inventory.domain.FulfillmentLocation;
import org.broadleafcommerce.inventory.service.FulfillmentLocationService;
import org.broadleafcommerce.openadmin.dto.Entity;
import org.broadleafcommerce.openadmin.dto.FieldMetadata;
import org.broadleafcommerce.openadmin.dto.PersistencePackage;
import org.broadleafcommerce.openadmin.dto.PersistencePerspective;
import org.broadleafcommerce.openadmin.server.dao.DynamicEntityDao;
import org.broadleafcommerce.openadmin.server.service.handler.CustomPersistenceHandlerAdapter;
import org.broadleafcommerce.openadmin.server.service.persistence.module.RecordHelper;

public class FulfillmentLocationCustomPersistenceHandler extends CustomPersistenceHandlerAdapter {

    private static final Log LOG = LogFactory.getLog(FulfillmentLocationCustomPersistenceHandler.class);

    @Resource(name = "blFulfillmentLocationService")
    protected FulfillmentLocationService fulfillmentLocationService;

    @Override
    public Boolean canHandleAdd(PersistencePackage persistencePackage) {
        return persistencePackage.getCeilingEntityFullyQualifiedClassname().equals(FulfillmentLocation.class.getName());
    }

    @Override
    public Boolean canHandleUpdate(PersistencePackage persistencePackage) {
        return canHandleAdd(persistencePackage);
    }

    @Override
    public Entity add(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        Entity entity = persistencePackage.getEntity();
        try {
            PersistencePerspective persistencePerspective = persistencePackage.getPersistencePerspective();
            FulfillmentLocation adminInstance = (FulfillmentLocation) Class.forName(entity.getType()[0]).newInstance();
            Map<String, FieldMetadata> adminProperties = helper.getSimpleMergedProperties(FulfillmentLocation.class.getName(), persistencePerspective);
            adminInstance = (FulfillmentLocation) helper.createPopulatedInstance(adminInstance, entity, adminProperties, false);

            //save the inventory instance first to give it an ID
            adminInstance = (FulfillmentLocation) dynamicEntityDao.merge(adminInstance);

            if (adminInstance.getDefaultLocation()) {
                fulfillmentLocationService.updateOtherDefaultLocationToFalse(adminInstance);
            }

            return helper.getRecord(adminProperties, adminInstance, null, null);
        } catch (Exception e) {
            LOG.error("Unable to add entity for " + entity.getType()[0], e);
            throw new ServiceException("Unable to add entity for " + entity.getType()[0], e);
        }
    }

    @Override
    public Entity update(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        Entity entity = persistencePackage.getEntity();
        try {
            PersistencePerspective persistencePerspective = persistencePackage.getPersistencePerspective();
            Map<String, FieldMetadata> adminProperties = helper.getSimpleMergedProperties(FulfillmentLocation.class.getName(), persistencePerspective);
            Object primaryKey = helper.getPrimaryKey(entity, adminProperties);
            FulfillmentLocation adminInstance = (FulfillmentLocation) dynamicEntityDao.retrieve(Class.forName(entity.getType()[0]), primaryKey);
            adminInstance = (FulfillmentLocation) helper.createPopulatedInstance(adminInstance, entity, adminProperties, false);

            if (adminInstance.getDefaultLocation()) {
                fulfillmentLocationService.updateOtherDefaultLocationToFalse(adminInstance);
            }

            adminInstance = (FulfillmentLocation) dynamicEntityDao.merge(adminInstance);

            return helper.getRecord(adminProperties, adminInstance, null, null);
        } catch (Exception e) {
            LOG.error("Unable to update entity for " + entity.getType()[0], e);
            throw new ServiceException("Unable to update entity for " + entity.getType()[0], e);
        }
    }

}
