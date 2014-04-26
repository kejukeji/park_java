/**
 * sudaw copy right 1.0 
 */
package org.broadleafcommerce.core.web.order.model;

import org.broadleafcommerce.core.order.domain.LocationedItem;
import org.broadleafcommerce.profile.web.core.CustomerState;

import com.ssbusy.core.account.domain.MyCustomer;

/**
 * AddToCartItemEx.java
 * 
 * @author jamesp
 */
public class AddToCartItemEx extends AddToCartItem {

	public void setLocationId(String locationId) {
		if (locationId == null)
			super.getItemAttributes().remove(
					LocationedItem.LOCATIONED_ITEM_LOCATION_ID);
		else
			super.getItemAttributes().put(
					LocationedItem.LOCATIONED_ITEM_LOCATION_ID, locationId);
	}

	public String getLocationId() {
		return super.getItemAttributes().get(
				LocationedItem.LOCATIONED_ITEM_LOCATION_ID);
	}

	/**
	 * 若未指定locationId，默认取当前region的第一个location
	 */
	public void validateLocationId() {
		if (this.getLocationId() == null) {
			MyCustomer c = (MyCustomer) CustomerState.getCustomer();
			if (c != null && c.getRegion() != null) {
				this.setLocationId(String.valueOf(c.getRegion()
						.getFulfillmentLocations().get(0).getId()));
			}
		}
	}
}
