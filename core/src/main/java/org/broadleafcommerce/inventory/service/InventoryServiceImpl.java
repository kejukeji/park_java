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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.broadleafcommerce.common.currency.domain.BroadleafCurrency;
import org.broadleafcommerce.common.persistence.EntityConfiguration;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.catalog.domain.Sku;
import org.broadleafcommerce.core.inventory.service.type.InventoryType;
import org.broadleafcommerce.inventory.dao.InventoryDao;
import org.broadleafcommerce.inventory.domain.FulfillmentLocation;
import org.broadleafcommerce.inventory.domain.Inventory;
import org.broadleafcommerce.inventory.exception.ConcurrentInventoryModificationException;
import org.broadleafcommerce.inventory.exception.InventoryUnavailableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("blInventoryService")
public class InventoryServiceImpl implements InventoryService {

	@Resource(name = "blInventoryDao")
	protected InventoryDao inventoryDao;

	@Resource(name = "blEntityConfiguration")
	protected EntityConfiguration entityConfiguration;

	@Override
	public boolean isSkuEligibleForInventoryCheck(Sku sku) {
		if (sku.getInventoryType() == null
				&& (sku.getProduct().getDefaultCategory() == null || sku
						.getProduct().getDefaultCategory().getInventoryType() == null)) {
			return false;
		} else if (InventoryType.NONE.equals(sku.getInventoryType())
				|| (sku.getProduct().getDefaultCategory() != null && InventoryType.NONE
						.equals(sku.getProduct().getDefaultCategory()
								.getInventoryType()))) {
			return false;
		} else if (InventoryType.BASIC.equals(sku.getInventoryType())
				|| (sku.getProduct().getDefaultCategory() != null && InventoryType.BASIC
						.equals(sku.getProduct().getDefaultCategory()
								.getInventoryType()))) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isQuantityAvailable(Sku sku, Integer quantity) {
		// if the sku does not exist or is not active, there is no quantity
		// available
		if (!sku.isActive()) {
			return false;
		}

		if (!isSkuEligibleForInventoryCheck(sku)) {
			// This sku is not eligible for inventory checks, so assume it is
			// available
			return true;
		}

		// quantity must be greater than 0
		if (quantity == null || quantity < 0) {
			throw new IllegalArgumentException(
					"Quantity must be a positive integer");
		}

		Inventory inventory = inventoryDao
				.readInventoryForDefaultFulfillmentLocation(sku);

		return inventory != null
				&& inventory.getQuantityAvailable() >= quantity;
	}

	@Override
	@Transactional("blTransactionManager")
	public boolean isQuantityAvailable(Sku sku, Integer quantity,
			FulfillmentLocation fulfillmentLocation) {

		// if the sku does not exist or is not active, there is no quantity
		// available
		if (!sku.isActive()) {
			return false;
		}

		if (!isSkuEligibleForInventoryCheck(sku)) {
			// This sku is not eligible for inventory checks, so assume it is
			// available
			return true;
		}

		// quantity must be greater than 0
		if (quantity == null || quantity < 0) {
			throw new IllegalArgumentException(
					"Quantity must be a positive integer");
		}

		Inventory inventory = inventoryDao.readInventory(sku,
				fulfillmentLocation);

		return inventory != null
				&& inventory.getQuantityAvailable() >= quantity;

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, value = "blTransactionManager", rollbackFor = {
			InventoryUnavailableException.class,
			ConcurrentInventoryModificationException.class })
	public void decrementInventory(Map<Sku, Integer> skuInventory)
			throws ConcurrentInventoryModificationException,
			InventoryUnavailableException {
		decrementInventory(skuInventory, null);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, value = "blTransactionManager", rollbackFor = {
			InventoryUnavailableException.class,
			ConcurrentInventoryModificationException.class })
	public void decrementInventory(Map<Sku, Integer> skuInventory,
			FulfillmentLocation fulfillmentLocation)
			throws ConcurrentInventoryModificationException,
			InventoryUnavailableException {

		Set<Sku> skus = skuInventory.keySet();
		Map<Long, Integer> unavailableInventoryHolder = new HashMap<Long, Integer>();
		Map<Sku, Integer> unavailableSkus = new HashMap<Sku, Integer>();
		for (Sku sku : skus) {

			Integer quantity = skuInventory.get(sku);

			/*
			 * If the inventory type of the sku or category is null or
			 * InventoryType.NONE, do not adjust inventory
			 */
			if (!isSkuEligibleForInventoryCheck(sku)) {
				// Don't adjust inventory for this Sku
				continue;
			}

			// quantity must not be null
			if (quantity == null || quantity < 0) {
				throw new IllegalArgumentException(
						"Quantity must not be a positive integer");
			}

			if (quantity == 0) {
				continue;
			}

			// check available inventory
			Inventory inventory = null;
			if (fulfillmentLocation != null) {
				inventory = inventoryDao.readInventoryForUpdate(sku,
						fulfillmentLocation);
			} else {
				inventory = inventoryDao
						.readInventoryForUpdateForDefaultFulfillmentLocation(sku);
			}

			if (inventory != null) {
				Integer quantityAvailable = inventory.getQuantityAvailable();

				int qtyToUpdate = quantityAvailable - quantity;
				if (qtyToUpdate < 0) {
					// there is not enough inventory available
					unavailableSkus.put(sku, quantityAvailable);
					unavailableInventoryHolder.put(sku.getId(),
							quantityAvailable);
				} else {
					inventory.setQuantityAvailable(qtyToUpdate);
					inventoryDao.save(inventory); // this call could throw
													// ConcurrentInventoryModificationException
				}

			} else {
				unavailableInventoryHolder.put(sku.getId(), 0);
			}

		}

		if (!unavailableInventoryHolder.isEmpty()) {
			String errorMessage = null;
			errorMessage = "<h3>有" + unavailableInventoryHolder.size()
					+ "个商品库存不足 请修改该商品的购买数量</h3><br/>商品名称为：<br/><br/>";
			for (Map.Entry<Sku, Integer> unavaiableSku : unavailableSkus
					.entrySet()) {
				errorMessage = errorMessage + unavaiableSku.getKey().getName()
						+ "；<br/>该商品还有" + unavaiableSku.getValue()
						+ "个库存。<br/><br/>";
			}
			InventoryUnavailableException ex = new InventoryUnavailableException(
					errorMessage);
			ex.setSkuInventoryAvailable(unavailableInventoryHolder);
			throw ex;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, value = "blTransactionManager", rollbackFor = {
			InventoryUnavailableException.class,
			ConcurrentInventoryModificationException.class })
	public void decrementInventoryOnHand(Map<Sku, Integer> skuInventory)
			throws ConcurrentInventoryModificationException,
			InventoryUnavailableException {
		decrementInventoryOnHand(skuInventory, null);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, value = "blTransactionManager", rollbackFor = {
			InventoryUnavailableException.class,
			ConcurrentInventoryModificationException.class })
	public void decrementInventoryOnHand(Map<Sku, Integer> skuInventory,
			FulfillmentLocation fulfillmentLocation)
			throws ConcurrentInventoryModificationException,
			InventoryUnavailableException {
		Set<Sku> skus = skuInventory.keySet();
		Map<Long, Integer> unavailableInventoryHolder = new HashMap<Long, Integer>();

		for (Sku sku : skus) {

			Integer quantity = skuInventory.get(sku);

			/*
			 * If the inventory type of the sku or category is null or
			 * InventoryType.NONE, do not adjust inventory
			 */
			if (!isSkuEligibleForInventoryCheck(sku)) {
				// Don't check inventory for this Sku
				continue;
			}

			// quantity must not be null
			if (quantity == null || quantity < 0) {
				throw new IllegalArgumentException(
						"Quantity must not be a positive integer");
			}

			if (quantity == 0) {
				continue;
			}

			// check available inventory
			Inventory inventory = null;
			if (fulfillmentLocation != null) {
				inventory = inventoryDao.readInventoryForUpdate(sku,
						fulfillmentLocation);
			} else {
				inventory = inventoryDao
						.readInventoryForUpdateForDefaultFulfillmentLocation(sku);
			}

			if (inventory != null) {
				Integer quantityOnHand = inventory.getQuantityOnHand();

				int qtyToUpdate = quantityOnHand - quantity;
				if (qtyToUpdate < 0) {
					// there is not enough inventory available
					unavailableInventoryHolder.put(sku.getId(), quantityOnHand);
				} else {
					inventory.setQuantityOnHand(qtyToUpdate);
					inventoryDao.save(inventory); // this call could throw
													// ConcurrentInventoryModificationException
				}
			} else {
				unavailableInventoryHolder.put(sku.getId(), 0);
			}

		}

		if (!unavailableInventoryHolder.isEmpty()) {
			InventoryUnavailableException ex = new InventoryUnavailableException(
					"Inventory is unavailable for "
							+ unavailableInventoryHolder.size() + " skus");
			ex.setSkuInventoryAvailable(unavailableInventoryHolder);
			throw ex;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, value = "blTransactionManager", rollbackFor = {
			InventoryUnavailableException.class,
			ConcurrentInventoryModificationException.class })
	public void incrementInventory(Map<Sku, Integer> skuInventory,
			FulfillmentLocation fulfillmentLocation)
			throws ConcurrentInventoryModificationException {
		Set<Sku> skus = skuInventory.keySet();

		for (Sku sku : skus) {
			Integer quantity = skuInventory.get(sku);

			/*
			 * If the inventory type of the sku or category is null or
			 * InventoryType.NONE, do not adjust inventory
			 */
			if (!isSkuEligibleForInventoryCheck(sku)) {
				// Don't adjust inventory for this Sku
				continue;
			}

			// quantity must not be null
			if (quantity == null || quantity < 0) {
				throw new IllegalArgumentException(
						"Quantity must not be a positive integer");
			}

			if (quantity == 0) {
				continue;
			}

			Inventory inventory = null;
			if (fulfillmentLocation != null) {
				inventory = inventoryDao.readInventoryForUpdate(sku,
						fulfillmentLocation);
			}

			if (inventory != null) {
				inventory.setQuantityAvailable(inventory.getQuantityAvailable()
						+ quantity);
				inventoryDao.save(inventory);
			} else {
				/*
				 * create a new inventory record if one does not exist
				 */
				inventory = (Inventory) entityConfiguration
						.createEntityInstance(Inventory.class.getName());
				inventory.setQuantityAvailable(quantity);
				inventory.setQuantityOnHand(quantity);
				inventory.setSku(sku);
				inventory.setFulfillmentLocation(fulfillmentLocation);
				inventoryDao.save(inventory);
			}

		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, value = "blTransactionManager", rollbackFor = {
			InventoryUnavailableException.class,
			ConcurrentInventoryModificationException.class })
	public void incrementInventory(Map<Sku, Integer> skuInventory)
			throws ConcurrentInventoryModificationException {

		Set<Sku> skus = skuInventory.keySet();
		for (Sku sku : skus) {
			Integer quantity = skuInventory.get(sku);

			/*
			 * If the inventory type of the sku or category is null or
			 * InventoryType.NONE, do not adjust inventory
			 */
			if (!isSkuEligibleForInventoryCheck(sku)) {
				// Don't adjust inventory for this Sku
				continue;
			}

			// quantity must not be null
			if (quantity == null || quantity < 0) {
				throw new IllegalArgumentException(
						"Quantity must not be a positive integer");
			}

			if (quantity == 0) {
				continue;
			}

			Inventory inventory = inventoryDao
					.readInventoryForUpdateForDefaultFulfillmentLocation(sku);

			if (inventory != null) {
				inventory.setQuantityAvailable(inventory.getQuantityAvailable()
						+ quantity);
				inventoryDao.save(inventory);
			} else {
				throw new IllegalStateException(
						"There was a call to InventoryServiceImpl.incrementInventory for a default fulfillment location, but no default "
								+ "inventory for the sku: "
								+ sku.getId()
								+ " could be found!");
			}

		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, value = "blTransactionManager", rollbackFor = {
			InventoryUnavailableException.class,
			ConcurrentInventoryModificationException.class })
	public void incrementInventoryOnHand(Map<Sku, Integer> skuInventory,
			FulfillmentLocation fulfillmentLocation)
			throws ConcurrentInventoryModificationException {
		Set<Sku> skus = skuInventory.keySet();
		for (Sku sku : skus) {
			Integer quantity = skuInventory.get(sku);

			/*
			 * If the inventory type of the sku or category is null or
			 * InventoryType.NONE, do not adjust inventory
			 */
			if (!isSkuEligibleForInventoryCheck(sku)) {
				// Don't adjust inventory for this Sku
				continue;
			}

			// quantity must not be null
			if (quantity == null || quantity < 0) {
				throw new IllegalArgumentException(
						"Quantity must not be a positive integer");
			}

			if (quantity == 0) {
				continue;
			}

			Inventory inventory = inventoryDao.readInventoryForUpdate(sku,
					fulfillmentLocation);

			if (inventory != null) {
				inventory.setQuantityOnHand(inventory.getQuantityOnHand()
						+ quantity);
				inventoryDao.save(inventory);
			} else {
				/*
				 * create a new inventory record if one does not exist
				 */
				inventory = (Inventory) entityConfiguration
						.createEntityInstance(Inventory.class.getName());
				inventory.setQuantityAvailable(quantity);
				inventory.setQuantityOnHand(quantity);
				inventory.setSku(sku);
				inventory.setFulfillmentLocation(fulfillmentLocation);
				inventoryDao.save(inventory);
			}

		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, value = "blTransactionManager", rollbackFor = {
			InventoryUnavailableException.class,
			ConcurrentInventoryModificationException.class })
	public void incrementInventoryOnHand(Map<Sku, Integer> skuInventory)
			throws ConcurrentInventoryModificationException {
		Set<Sku> skus = skuInventory.keySet();
		for (Sku sku : skus) {
			Integer quantity = skuInventory.get(sku);

			/*
			 * If the inventory type of the sku or category is null or
			 * InventoryType.NONE, do not adjust inventory
			 */
			if (!isSkuEligibleForInventoryCheck(sku)) {
				// Don't adjust inventory for this Sku
				continue;
			}

			// quantity must not be null
			if (quantity == null || quantity < 0) {
				throw new IllegalArgumentException(
						"Quantity must not be a positive integer");
			}

			if (quantity == 0) {
				continue;
			}

			Inventory inventory = inventoryDao
					.readInventoryForUpdateForDefaultFulfillmentLocation(sku);

			if (inventory != null) {
				inventory.setQuantityOnHand(inventory.getQuantityOnHand()
						+ quantity);
				inventoryDao.save(inventory);
			} else {
				throw new IllegalStateException(
						"There was a call to InventoryServiceImpl.incrementInventoryOnHand for a default fulfillment location, but no default "
								+ "inventory for the sku: "
								+ sku.getId()
								+ " could be found!");
			}
		}
	}

	@Override
	@Transactional(value = "blTransactionManager")
	public Inventory readInventory(Sku sku,
			FulfillmentLocation fulfillmentLocation) {
		return inventoryDao.readInventory(sku, fulfillmentLocation);
	}

	@Override
	@Transactional(value = "blTransactionManager")
	public Inventory readInventory(Sku sku) {
		return inventoryDao.readInventoryForDefaultFulfillmentLocation(sku);
	}

	@Override
	public List<Inventory> listInventories(Sku sku) {
		return inventoryDao.listInventories(sku);
	}

	@Override
	public List<Inventory> listAllInventories(Product product,
			List<FulfillmentLocation> fulfillmentLocations) {
		return inventoryDao.listAllInventories(product, fulfillmentLocations);
	}

	@Override
	public Map<Product, List<Inventory>> listAllInventories(
			List<Product> products, List<FulfillmentLocation> locations) {
		if (products == null || products.isEmpty() || locations == null
				|| locations.isEmpty())
			return Collections.emptyMap();
		List<Inventory> inventories = inventoryDao.listAllInventories(products,
				locations);
		Map<Product, List<Inventory>> ret = new HashMap<Product, List<Inventory>>(
				inventories.size());
		for (Inventory i : inventories) {
			Product p = i.getSku().getProduct();
			List<Inventory> list = ret.get(p);
			if (list == null)
				ret.put(p, list = new ArrayList<Inventory>());
			list.add(i);
		}
		return ret;
	}

	@Override
	@Transactional(value = "blTransactionManager")
	public List<Inventory> readInventoryForFulfillmentLocation(
			FulfillmentLocation fulfillmentLocation) {
		return inventoryDao
				.readInventoryForFulfillmentLocation(fulfillmentLocation);
	}

	@Override
	@Transactional(value = "blTransactionManager")
	public Inventory save(Inventory inventory)
			throws ConcurrentInventoryModificationException {
		return inventoryDao.save(inventory);
	}

	@Override
	@Transactional(value = "blTransactionManager")
	public List<Sku> readSkusNotAtFulfillmentLocation(
			FulfillmentLocation fulfillmentLocation) {
		return inventoryDao
				.readSkusNotAtFulfillmentLocation(fulfillmentLocation);
	}

	@Override
	public Boolean isAllQuantityAvailable(Sku sku, Integer quantity) {
		// if the sku does not exist or is not active, there is no quantity
		// available
		if (!sku.isActive()) {
			return false;
		}

		if (!isSkuEligibleForInventoryCheck(sku)) {
			// This sku is not eligible for inventory checks, so assume it is
			// available
			return true;
		}

		// quantity must be greater than 0
		if (quantity == null || quantity < 0) {
			throw new IllegalArgumentException(
					"Quantity must be a positive integer");
		}

		Long couont = inventoryDao
				.readInventoryForSkuAndQuantity(sku, quantity);

		return (couont <= 0);
	}

	@Override
	public List<Inventory> readInventoryForLessThanQuantity(Integer quantity,
			Long fulfillmentLocationId) {
		if (quantity == null || quantity < 0) {
			throw new IllegalArgumentException(
					"Quantity must be a positive integer");
		}
		return inventoryDao.readInventoryForLessThanQuantity(quantity,
				fulfillmentLocationId);
	}

	@Override
	public boolean isInventory(Product product, FulfillmentLocation location) {
		if (isSkuEligibleForInventoryCheck(product.getDefaultSku()))
			return inventoryDao.countInventory(product, location) > 0;
		return true;
	}

	@Override
	public List<Sku> listAllSkus(Product product,
			FulfillmentLocation fulfillmentLocation) {
		if (isSkuEligibleForInventoryCheck(product.getDefaultSku()))
			return inventoryDao.listAllSkus(product, fulfillmentLocation);

		List<Sku> skus = product.getSkus();
		List<Sku> ret = new ArrayList<Sku>(skus.size() + 1);
		ret.add(product.getDefaultSku());
		ret.addAll(skus);
		return ret;
	}

	@Override
	public Boolean skuIsInFulfillmentLocation(Sku sku,
			FulfillmentLocation fulfillmentLocation) {
		if (!sku.isActive()) {
			return false;
		}

		if (!isSkuEligibleForInventoryCheck(sku)) {
			// This sku is not eligible for inventory checks, so assume it is
			// available
			return true;
		}
		return inventoryDao
				.skuIsInFulfillmentLocation(sku, fulfillmentLocation);
	}

	@Override
	public List<Product> filterProducts(final List<Product> products,
			List<FulfillmentLocation> locs) {
		if (products == null || products.isEmpty() || locs == null
				|| locs.isEmpty())
			return products;

		// FIXME isSkuEligibleForInventoryCheck for each product

		List<Product> prods = inventoryDao.filterProducts(products, locs);
		Collections.sort(prods, new Comparator<Product>() {
			@Override
			public int compare(Product o1, Product o2) {
				return products.indexOf(o1) - products.indexOf(o2);
			}
		});
		return prods;
	}

	@Override
	public List<Sku> filterSkus(final List<Sku> skus, FulfillmentLocation loc) {
		if (skus == null || skus.isEmpty())
			return skus;
		List<Sku> ss = inventoryDao.filterSkus(skus, loc);
		Collections.sort(ss, new Comparator<Sku>() {
			@Override
			public int compare(Sku o1, Sku o2) {
				return skus.indexOf(o1) - skus.indexOf(o2);
			}
		});
		return ss;
	}

	@Override
	public void incrementInventory(Inventory inventory, Integer quantity)
			throws ConcurrentInventoryModificationException {
		// quantity must not be null
		if (quantity == null || quantity < 0) {
			throw new IllegalArgumentException(
					"Quantity must not be a positive integer");
		}

		Inventory inventorys = inventoryDao.readById(inventory.getId());

		if (inventorys != null) {
			inventorys.setQuantityOnHand(inventorys.getQuantityOnHand()
					+ quantity);
			inventorys.setQuantityAvailable(inventorys.getQuantityAvailable()
					+ quantity);
			inventoryDao.save(inventory);
		}
	}

	@Override
	public List<Inventory> findProductsByPriceAndCurrency(BigDecimal amount,
			BroadleafCurrency currency,
			List<FulfillmentLocation> fulfillmentLocations, int start, int size) {
		if (amount.intValue() != amount.floatValue()) {
			return inventoryDao.readSkuByPriceAndCurrency(amount, currency,
					fulfillmentLocations, start, size);
		} else {
			return Collections.emptyList();
		}
	}

}
