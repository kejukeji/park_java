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
package org.broadleafcommerce.inventory.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.broadleafcommerce.inventory.domain.FulfillmentLocation;
import org.broadleafcommerce.inventory.domain.FulfillmentLocationImpl;
import org.hibernate.ejb.QueryHints;
import org.springframework.stereotype.Repository;

@Repository("blFulfillmentLocationDao")
public class FulfillmentLocationDaoImpl implements FulfillmentLocationDao {

    @PersistenceContext(unitName="blPU")
    protected EntityManager em;

    @SuppressWarnings("unchecked")
    @Override
    public List<FulfillmentLocation> readAll() {
        Query query = em.createNamedQuery("BC_READ_ALL_FULFILLMENT_LOCATIONS");
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        return query.getResultList();
    }

    @Override
    public FulfillmentLocation readById(Long fulfillmentLocationId) {
        return em.find(FulfillmentLocationImpl.class, fulfillmentLocationId);
    }

    @Override
    public FulfillmentLocation save(FulfillmentLocation fulfillmentLocation) {
        return em.merge(fulfillmentLocation);
    }

    @Override
    public void delete(FulfillmentLocation fulfillmentLocation) {
        em.remove(fulfillmentLocation);
    }

    @Override
    public void updateOtherDefaultLocationToFalse(FulfillmentLocation fulfillmentLocation) {
        Query query = em.createNamedQuery("BC_UPDATE_ALL_FULFILLMENT_LOCATIONS_TO_NOT_DEFAULT");
        query.setParameter("fulfillmentLocationId", fulfillmentLocation.getId());
        query.executeUpdate();
    }

    @Override
    public FulfillmentLocation readDefaultFulfillmentLocation() {
        Query query = em.createNamedQuery("BC_READ_DEFAULT_FULFILLMENT_LOCATION");
        query.setMaxResults(1);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        @SuppressWarnings("rawtypes")
        List results = query.getResultList();
        if (results != null && !results.isEmpty()) {
            return (FulfillmentLocation) results.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<FulfillmentLocation> readAllFulfillmentLocationsForSku(Long skuId) {
        Query query = em.createNamedQuery("BC_READ_FULFILLMENT_LOCATIONS_FOR_SKU");
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setParameter("skuId", skuId);
        return query.getResultList();
    }

}
