/**
 * Copyright 2012 the original author or authors.
 * <p/>
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

import org.broadleafcommerce.common.currency.domain.BroadleafCurrency;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.catalog.domain.Sku;
import org.broadleafcommerce.inventory.domain.FulfillmentLocation;
import org.broadleafcommerce.inventory.domain.Inventory;
import org.broadleafcommerce.inventory.exception.ConcurrentInventoryModificationException;

public interface InventoryDao {

	public Inventory readById(Long id);

	/**
	 * Same as read, but refreshes and handles locking for concurrent updates
	 * 
	 * @param id
	 * @return
	 */
	public Inventory readForUpdateById(Long id)
			throws ConcurrentInventoryModificationException;

	/**
	 * Retrieves the {@link Inventory} for the given {@link Sku} and
	 * {@link FulfillmentLocation}
	 * 
	 * @param sku
	 *            {@link Sku}
	 * @param fulfillmentLocation
	 *            {@link FulfillmentLocation}
	 * @return {@link Inventory}
	 */
	public Inventory readInventory(Sku sku,
			FulfillmentLocation fulfillmentLocation);

	/**
	 * Same as readInventory but refreshes and locks the object.
	 * 
	 * @param sku
	 * @param fulfillmentLocation
	 * @return
	 * @throws ConcurrentInventoryModificationException
	 */
	public Inventory readInventoryForUpdate(Sku sku,
			FulfillmentLocation fulfillmentLocation)
			throws ConcurrentInventoryModificationException;

	/**
	 * Retrieves the {@link Inventory} for the given {@link Sku}
	 * 
	 * @param sku
	 *            {@link org.broadleafcommerce.core.catalog.domain.Sku}
	 * @return {@link List}
	 */
	public Inventory readInventoryForDefaultFulfillmentLocation(Sku sku);

	/**
	 * Same as readInventoryForDefaultFulfillmentLocation but refreshes and
	 * locks the object.
	 * 
	 * @param sku
	 * @return
	 * @throws ConcurrentInventoryModificationException
	 */
	public Inventory readInventoryForUpdateForDefaultFulfillmentLocation(Sku sku)
			throws ConcurrentInventoryModificationException;

	/**
	 * Persists the {@link Inventory}
	 * 
	 * @param inventory
	 *            {@link Inventory}
	 * @return the persisted {@link Inventory}
	 */
	public Inventory save(Inventory inventory)
			throws ConcurrentInventoryModificationException;

	/**
	 * Deletes the {@link Inventory}
	 * 
	 * @param inventory
	 */
	public void delete(Inventory inventory);

	/**
	 * Retrieves all instances of Inventory for this fulfillmentLocation
	 * 
	 * @param fulfillmentLocation
	 * @return list of {@link Inventory}
	 */
	public List<Inventory> readInventoryForFulfillmentLocation(
			FulfillmentLocation fulfillmentLocation);

	/**
	 * Retrieves skus that do not have inventory records at a particular
	 * fulfillment location
	 * 
	 * @param fulfillmentLocation
	 * @return
	 */
	public List<Sku> readSkusNotAtFulfillmentLocation(
			FulfillmentLocation fulfillmentLocation);

	/**
	 * @param sku
	 * @param quantity
	 * @return
	 */
	public Long readInventoryForSkuAndQuantity(Sku sku, Integer quantity);

	/**
	 * 根据提供的数量查找小于该数量的sku
	 * 
	 * @param quantity
	 * @return
	 */
	public List<Inventory> readInventoryForLessThanQuantity(Integer quantity,
			Long fulfillmentLocationId);

	public long countInventory(Product product, FulfillmentLocation location);

	public List<Sku> listAllSkus(Product product,
			FulfillmentLocation fulfillmentLocation);

	public boolean skuIsInFulfillmentLocation(Sku sku,
			FulfillmentLocation fulfillmentLocation);

	public List<Product> filterProducts(List<Product> products,
			List<FulfillmentLocation> locs);

	public List<Sku> filterSkus(List<Sku> skus, FulfillmentLocation location);

	/**
	 * @param sku
	 * @return all inventories for the sku.
	 */
	public List<Inventory> listInventories(Sku sku);

	/**
	 * @param product
	 * @param fulfillmentLocations
	 * @return
	 */
	public List<Inventory> listAllInventories(Product product,
			List<FulfillmentLocation> fulfillmentLocations);

	/**
	 * @param products
	 * @param locations
	 * @return
	 */
	public List<Inventory> listAllInventories(
			List<Product> products, List<FulfillmentLocation> locations);

	/**
	 * @param amount
	 * @param currency
	 * @param fulfillmentLocations
	 * @param start
	 * @param size
	 * @return
	 */
	public List<Inventory> readSkuByPriceAndCurrency(BigDecimal amount,
			BroadleafCurrency currency,
			List<FulfillmentLocation> fulfillmentLocations, int start, int size);
}
