/**
 * Copyright 2012 the original author or authors.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.broadleafcommerce.inventory.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.broadleafcommerce.common.currency.domain.BroadleafCurrency;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.catalog.domain.Sku;
import org.broadleafcommerce.inventory.domain.FulfillmentLocation;
import org.broadleafcommerce.inventory.domain.Inventory;
import org.broadleafcommerce.inventory.exception.ConcurrentInventoryModificationException;
import org.broadleafcommerce.inventory.exception.InventoryUnavailableException;

/**
 * This is a basic inventory service for Broadleaf Commerce. This API defines a
 * basic set of functions for checking inventory availability and for adjusting
 * inventory.
 * NOTE: If you wrap this service inside another service or transactional
 * component, it may be best to ensure that transactions are rolled back when
 * encountering checked exceptions that are thrown from this service, such as
 * {@link InventoryUnavailableException} and
 * {@link ConcurrentInventoryModificationException}
 * 
 * @author Kelly Tisdell
 */
public interface InventoryService {

	/**
	 * Tests whether an inventory check will be invoked for the given Sku.
	 * 
	 * @param sku
	 * @return
	 */
	public boolean isSkuEligibleForInventoryCheck(Sku sku);

	/**
	 * Retrieves whether or not the quantity is available for a sku at all
	 * fulfillment locations.
	 * 
	 * @param sku
	 *            the sku
	 * @param quantity
	 *            the amount for which to check; must be a positive integer
	 * @return the boolean result of whether or not the quantity is available
	 */
	public boolean isQuantityAvailable(Sku sku, Integer quantity);

	/**
	 * Retrieves whether or not the quantity is available for a sku at a
	 * fulfillment location. If fulfillmentLocation is not supplied, this will
	 * check all fulfillment locations for availability.
	 * 
	 * @param sku
	 *            the sku
	 * @param quantity
	 *            the amount for which to check; must be a positive integer
	 * @param fulfillmentLocation
	 *            the fulfillment location
	 * @return boolean result of whether or not the specified quantity is
	 *         available
	 */
	public boolean isQuantityAvailable(Sku sku, Integer quantity,
			FulfillmentLocation fulfillmentLocation);

	/**
	 * Subtracts the quantity from available inventory in the default
	 * fulfillment location for each sku in the map. Specified quantity must be
	 * a positive integer.
	 * 
	 * @param skuInventory
	 *            a map which contains the quantity of inventory to subtract
	 *            from available inventory for each sku
	 */
	public void decrementInventory(Map<Sku, Integer> skuInventory)
			throws ConcurrentInventoryModificationException,
			InventoryUnavailableException;

	/**
	 * Subtracts the quantity from available inventory for each sku in the map
	 * for the given fulfillment location. Quantity must be a positive integer.
	 * 
	 * @param skuInventory
	 *            a map which contains the quantity of inventory to subtract
	 *            from available inventory for each sku
	 * @param fulfillmentLocation
	 *            the fulfillment location
	 */
	public void decrementInventory(Map<Sku, Integer> skuInventory,
			FulfillmentLocation fulfillmentLocation)
			throws ConcurrentInventoryModificationException,
			InventoryUnavailableException;

	/**
	 * Subtracts the quantity from inventory on hand in the default fulfillment
	 * location for each sku in the map. Specified quantity must be a positive
	 * integer. This is typically done when the items are shipped or fulfilled.
	 * 
	 * @param skuInventory
	 * @throws ConcurrentInventoryModificationException
	 * @throws InventoryUnavailableException
	 */
	public void decrementInventoryOnHand(Map<Sku, Integer> skuInventory)
			throws ConcurrentInventoryModificationException,
			InventoryUnavailableException;

	/**
	 * Subtracts the quantity from inventory on hand for each sku in the map for
	 * the given fulfillment location. Specified quantity must be a positive
	 * integer. This is typically done when inventory is shipped or fulfilled.
	 * 
	 * @param skuInventory
	 * @param fulfillmentLocation
	 * @throws ConcurrentInventoryModificationException
	 * @throws InventoryUnavailableException
	 */
	public void decrementInventoryOnHand(Map<Sku, Integer> skuInventory,
			FulfillmentLocation fulfillmentLocation)
			throws ConcurrentInventoryModificationException,
			InventoryUnavailableException;

	/**
	 * Add available inventory to sku. If fulfillment location is null, this
	 * method throws an {@link IllegalArgumentException}.
	 * 
	 * @param skuInventory
	 * @param fulfillmentLocation
	 * @throws ConcurrentInventoryModificationException
	 */
	public void incrementInventory(Map<Sku, Integer> skuInventory,
			FulfillmentLocation fulfillmentLocation)
			throws ConcurrentInventoryModificationException;

	/**
	 * Attempts to add available inventory to an inventory record associated
	 * with a default fulfillment location.
	 * 
	 * @param skuInventory
	 * @throws ConcurrentInventoryModificationException
	 * @throws IllegalStateException
	 *             if no default fulfillment location could be found for the
	 *             Sku.
	 */
	public void incrementInventory(Map<Sku, Integer> skuInventory)
			throws ConcurrentInventoryModificationException;

	/**
	 * Add inventory on hand to sku. If fulfillment location is null, this
	 * method throws an {@link IllegalArgumentException}.
	 * 
	 * @param skuInventory
	 * @param fulfillmentLocation
	 * @throws ConcurrentInventoryModificationException
	 */
	public void incrementInventoryOnHand(Map<Sku, Integer> skuInventory,
			FulfillmentLocation fulfillmentLocation)
			throws ConcurrentInventoryModificationException;

	/**
	 * Attempts to add inventory on hand to an inventory record associated with
	 * a default fulfillment location.
	 * 
	 * @param skuInventory
	 * @throws ConcurrentInventoryModificationException
	 */
	public void incrementInventoryOnHand(Map<Sku, Integer> skuInventory)
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
	 * Lists all inventory for this sku.
	 * 
	 * @param sku
	 * @return
	 */
	public List<Inventory> listInventories(Sku sku);

	/**
	 * Retrieves inventory for this sku at the default fulfillment location.
	 * 
	 * @param sku
	 * @return
	 */
	public Inventory readInventory(Sku sku);

	/**
	 * Retrieves all instances of Inventory for this fulfillmentLocation
	 * 
	 * @param fulfillmentLocation
	 * @return list of {@link Inventory}
	 */
	public List<Inventory> readInventoryForFulfillmentLocation(
			FulfillmentLocation fulfillmentLocation);

	/**
	 * Persists the inventory
	 */
	public Inventory save(Inventory inventory)
			throws ConcurrentInventoryModificationException;

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
	 * @return <code>true</code>表示所有仓库库存都充足； <code>false</code>表示至少有一个仓库库存小于购买量；
	 *         <code>null</code>待定；
	 */
	public Boolean isAllQuantityAvailable(Sku sku, Integer quantity);

	/**
	 * 根据提供的数量查找库存小于该数量的inventory
	 * 
	 * @param quantity
	 * @return
	 */
	public List<Inventory> readInventoryForLessThanQuantity(Integer quantity,
			Long fulfillmentLocationId);

	/**
	 * 该product是否有该location的inventory记录
	 * 
	 * @param product
	 * @param location
	 * @return
	 */
	public boolean isInventory(Product product, FulfillmentLocation location);

	/**
	 * 包括默认sku
	 * 
	 * @param product
	 * @param fulfillmentLocation
	 * @return
	 */
	public List<Sku> listAllSkus(Product product,
			FulfillmentLocation fulfillmentLocation);

	/**
	 * @param sku
	 * @param fulfillmentLocation
	 * @return <code>true</code> 该仓库有该sku；<code>false</code>该仓库没有该sku；
	 */
	public Boolean skuIsInFulfillmentLocation(Sku sku,
			FulfillmentLocation fulfillmentLocation);

	/**
	 * @returns 依旧保证list顺序
	 */
	public List<Product> filterProducts(List<Product> products,
			List<FulfillmentLocation> locs);

	public List<Sku> filterSkus(final List<Sku> skus, FulfillmentLocation loc);

	public void incrementInventory(Inventory inventory, Integer quantity)
			throws ConcurrentInventoryModificationException;

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
	public Map<Product, List<Inventory>> listAllInventories(
			List<Product> products, List<FulfillmentLocation> locations);

	/**
	 * 根据金额和分仓查询凑整商品信息
	 * 
	 * @param amount
	 * @param currency
	 * @param fulfillmentLocations
	 * @param start
	 * @param size
	 * @return
	 */
	public List<Inventory> findProductsByPriceAndCurrency(BigDecimal amount,
			BroadleafCurrency currency,
			List<FulfillmentLocation> fulfillmentLocations, int start, int size);
}
