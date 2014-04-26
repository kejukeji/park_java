/**
 * sudaw copy right 1.0 
 */
package org.broadleafcommerce.core.order.domain;

import org.broadleafcommerce.inventory.domain.FulfillmentLocation;

/**
 * item来自哪个仓
 * LocationedItem.java
 * 
 * @author jamesp
 */
public interface LocationedItem {
	static final String LOCATIONED_ITEM_LOCATION_ID = "LOCATIONED_ITEM_LOCATION_ID";

	FulfillmentLocation getLocation();

	void setLocation(FulfillmentLocation location);
}
