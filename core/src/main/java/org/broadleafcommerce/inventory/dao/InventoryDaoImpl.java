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

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.broadleafcommerce.common.currency.domain.BroadleafCurrency;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.catalog.domain.Sku;
import org.broadleafcommerce.inventory.domain.FulfillmentLocation;
import org.broadleafcommerce.inventory.domain.Inventory;
import org.broadleafcommerce.inventory.domain.InventoryImpl;
import org.broadleafcommerce.inventory.exception.ConcurrentInventoryModificationException;
import org.springframework.stereotype.Repository;

@Repository("blInventoryDao")
public class InventoryDaoImpl implements InventoryDao {

	@PersistenceContext(unitName = "blPU")
	protected EntityManager em;

	@Override
	public Inventory save(Inventory inventory)
			throws ConcurrentInventoryModificationException {
		try {
			inventory = em.merge(inventory);

			// This should cause an OptimisticLockException immediately if
			// someone has
			// already modified this object, rather than waiting for the
			// transaction to complete.
			em.flush();
			return inventory;
		} catch (OptimisticLockException ex) {
			throw new ConcurrentInventoryModificationException(
					"Error saving inventory with id: " + inventory.getId());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Inventory readInventory(Sku sku,
			FulfillmentLocation fulfillmentLocation) {
		Query query = em.createNamedQuery("BC_READ_SKU_INVENTORY_FOR_LOCATION");
		query.setParameter("skuId", sku.getId());
		query.setParameter("fulfillmentLocationId", fulfillmentLocation.getId());

		List<Inventory> inventories = query.getResultList();
		if (CollectionUtils.isNotEmpty(inventories)) {
			return inventories.get(0);
		}

		return null;
	}

	@Override
	public Inventory readInventoryForUpdate(Sku sku,
			FulfillmentLocation fulfillmentLocation)
			throws ConcurrentInventoryModificationException {
		Inventory inventory = readInventory(sku, fulfillmentLocation);
		if (inventory != null) {
			try {
				em.refresh(inventory, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
				em.flush();
			} catch (OptimisticLockException ex) {
				throw new ConcurrentInventoryModificationException(
						"Error locking inventory object with id: "
								+ inventory.getId());
			}
		}
		return inventory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Inventory readInventoryForDefaultFulfillmentLocation(Sku sku) {
		Query query = em
				.createNamedQuery("BC_READ_SKU_INVENTORY_FOR_DEFAULT_LOCATION");
		query.setParameter("skuId", sku.getId());
		query.setMaxResults(1);
		List<Inventory> inventories = query.getResultList();
		if (CollectionUtils.isNotEmpty(inventories)) {
			return inventories.get(0);
		}
		return null;
	}

	@Override
	public Inventory readInventoryForUpdateForDefaultFulfillmentLocation(Sku sku)
			throws ConcurrentInventoryModificationException {
		Inventory inventory = readInventoryForDefaultFulfillmentLocation(sku);
		if (inventory != null) {
			try {
				em.refresh(inventory, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
				em.flush();
			} catch (OptimisticLockException ex) {
				throw new ConcurrentInventoryModificationException(
						"Error locking inventory object with id: "
								+ inventory.getId());
			}
		}
		return inventory;
	}

	@Override
	public void delete(Inventory inventory) {
		em.remove(inventory);
	}

	@Override
	public Inventory readById(Long id) {
		return em.find(InventoryImpl.class, id);
	}

	@Override
	public Inventory readForUpdateById(Long id)
			throws ConcurrentInventoryModificationException {
		Inventory inventory = readById(id);
		if (inventory != null) {
			try {
				em.refresh(inventory, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
				em.flush();
			} catch (OptimisticLockException ex) {
				throw new ConcurrentInventoryModificationException(
						"Error locking inventory object with id: "
								+ inventory.getId());
			}
		}
		return inventory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Inventory> readInventoryForFulfillmentLocation(
			FulfillmentLocation fulfillmentLocation) {
		Query query = em
				.createNamedQuery("BC_READ_INVENTORY_FOR_FULFILLMENT_LOCATION");
		query.setParameter("fulfillmentLocationId", fulfillmentLocation.getId());
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Inventory> listInventories(Sku sku) {
		Query query = em.createNamedQuery("BC_READ_INVENTORIES_FOR_SKU");
		query.setParameter("skuId", sku.getId());
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Inventory> listAllInventories(Product product,
			List<FulfillmentLocation> fulfillmentLocations) {
		Query query = em.createNamedQuery("BC_READ_INVENTORIES_FOR_PRODUCT");
		query.setParameter("product", product);
		query.setParameter("locations", fulfillmentLocations);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Inventory> listAllInventories(
			List<Product> products, List<FulfillmentLocation> locations) {
		Query query = em.createNamedQuery("BC_READ_INVENTORIES_FOR_PRODUCTS");
		query.setParameter("products", products);
		query.setParameter("locations", locations);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Sku> readSkusNotAtFulfillmentLocation(
			FulfillmentLocation fulfillmentLocation) {
		Query query = em
				.createNamedQuery("BC_READ_SKUS_NOT_AT_FULFILLMENT_LOCATION");
		query.setParameter("fulfillmentLocationId", fulfillmentLocation.getId());
		return query.getResultList();
	}

	@Override
	public Long readInventoryForSkuAndQuantity(Sku sku, Integer quantity) {
		Query query = em
				.createNamedQuery("BC_READ_SKU_INVENTORY_FOR_QUANTITY_AVAILABLE");
		query.setParameter("skuId", sku.getId());
		query.setParameter("quantity", quantity);
		return (Long) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Inventory> readInventoryForLessThanQuantity(Integer quantity,
			Long fulfillmentLocationId) {
		Query query = em.createNamedQuery("BC_READ_INVENTORY_FOR_QUANTITY");
		query.setParameter("quantity", quantity);
		query.setParameter("fulfillmentLocationId", fulfillmentLocationId);
		return query.getResultList();
	}

	@Override
	public long countInventory(Product product, FulfillmentLocation location) {
		Query query = em
				.createNamedQuery("BC_COUNT_PRODUCT_IN_INVENTORY_FOR_LOC");
		query.setParameter("product", product);
		query.setParameter("location", location);
		return (Long) query.getSingleResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Sku> listAllSkus(Product product,
			FulfillmentLocation fulfillmentLocation) {
		Query query = em.createNamedQuery("BC_LIST_PRODUCT_SKUS_FOR_LOC");
		query.setParameter("product", product);
		query.setParameter("location", fulfillmentLocation);
		return query.getResultList();
	}

	@Override
	public boolean skuIsInFulfillmentLocation(Sku sku,
			FulfillmentLocation fulfillmentLocation) {
		Query query = em.createNamedQuery("BC_COUNT_SKU_FOR_LOCATION");
		query.setParameter("skuId", sku.getId());
		query.setParameter("fulfillmentLocationId", fulfillmentLocation.getId());
		return (Long) query.getSingleResult() > 0 ? true : false;
		// Query query = em
		// .createNamedQuery("BC_COUNT_PRODUCT_IN_INVENTORY_FOR_LOC");
		// query.setParameter("product", sku.getProduct());
		// query.setParameter("location", fulfillmentLocation);
		// return (Long) query.getSingleResult() > 0 ? true : false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> filterProducts(List<Product> products,
			List<FulfillmentLocation> locations) {
		Query query = em.createNamedQuery("BC_FILTER_PRODUCTS_FOR_LOC");
		query.setParameter("products", products);
		query.setParameter("locations", locations);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Sku> filterSkus(List<Sku> skus, FulfillmentLocation location) {
		Query query = em.createNamedQuery("BC_FILTER_SKUS_FOR_LOC");
		query.setParameter("skus", skus);
		query.setParameter("location", location);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Inventory> readSkuByPriceAndCurrency(BigDecimal amount,
			BroadleafCurrency currency,
			List<FulfillmentLocation> locations, int start, int size) {
		Query query = em.createNamedQuery("SSB_READ_INVS_BY_PRICE_CURRENCY");
		query.setParameter("price", amount);
		query.setParameter("currency", currency);
		query.setParameter("locations", locations);
		query.setFirstResult(start);
		query.setMaxResults(size);
		return query.getResultList();
	}
}
