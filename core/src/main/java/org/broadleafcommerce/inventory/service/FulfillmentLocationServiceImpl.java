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
package org.broadleafcommerce.inventory.service;

import java.util.List;

import javax.annotation.Resource;

import org.broadleafcommerce.inventory.dao.FulfillmentLocationDao;
import org.broadleafcommerce.inventory.domain.FulfillmentLocation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("blFulfillmentLocationService")
@Transactional(value="blTransactionManager")
public class FulfillmentLocationServiceImpl implements FulfillmentLocationService {

    @Resource(name = "blFulfillmentLocationDao")
    protected FulfillmentLocationDao fulfillmentLocationDao;

    @Override
    @Transactional("blTransactionManager")
    public List<FulfillmentLocation> readAll() {
        return fulfillmentLocationDao.readAll();
    }

    @Override
    @Transactional("blTransactionManager")
    public FulfillmentLocation readById(Long fulfillmentLocationId) {
        return fulfillmentLocationDao.readById(fulfillmentLocationId);
    }

    @Override
    @Transactional("blTransactionManager")
    public FulfillmentLocation save(FulfillmentLocation fulfillmentLocation) {
        return fulfillmentLocationDao.save(fulfillmentLocation);
    }

    @Override
    @Transactional("blTransactionManager")
    public void delete(FulfillmentLocation fulfillmentLocation) {
        fulfillmentLocationDao.delete(fulfillmentLocation);
    }

    @Override
    @Transactional("blTransactionManager")
    public void updateOtherDefaultLocationToFalse(FulfillmentLocation fulfillmentLocation) {
        fulfillmentLocationDao.updateOtherDefaultLocationToFalse(fulfillmentLocation);
    }

    @Override
    @Transactional("blTransactionManager")
    public FulfillmentLocation findDefaultFulfillmentLocation() {
        return fulfillmentLocationDao.readDefaultFulfillmentLocation();
    }

    @Override
    @Transactional("blTransactionManager")
    public List<FulfillmentLocation> findAllFulfillmentLocationsForSku(Long skuId) {
        return fulfillmentLocationDao.readAllFulfillmentLocationsForSku(skuId);
    }
}
