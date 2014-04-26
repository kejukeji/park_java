package org.broadleafcommerce.inventory.service.workflow;

import java.io.Serializable;
import java.util.Map;

import org.broadleafcommerce.core.catalog.domain.Sku;
import org.broadleafcommerce.inventory.domain.FulfillmentLocation;

/**
 * Contains the inventory state in case of a rollback.
 * 
 * @author Kelly Tisdell
 *
 */
public class InventoryState implements Serializable {

    private static final long serialVersionUID = 1L;

    private FulfillmentLocation fulfillmentLocation;

    private Map<Sku, Integer> skuQuantityMap;

    public Map<Sku, Integer> getSkuQuantityMap() {
        return skuQuantityMap;
    }

    public void setSkuQuantityMap(Map<Sku, Integer> skuQuantityMap) {
        this.skuQuantityMap = skuQuantityMap;
    }

    public FulfillmentLocation getFulfillmentLocation() {
        return fulfillmentLocation;
    }

    public void setFulfillmentLocation(FulfillmentLocation fulfillmentLocation) {
        this.fulfillmentLocation = fulfillmentLocation;
    }
}
