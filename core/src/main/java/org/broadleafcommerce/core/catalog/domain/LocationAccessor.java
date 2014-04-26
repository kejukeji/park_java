package org.broadleafcommerce.core.catalog.domain;

import org.broadleafcommerce.inventory.domain.FulfillmentLocation;

public interface LocationAccessor extends PromotableProduct {

	static final Long SEQUANCE_FACTOR = 100000L;

	FulfillmentLocation getLocation();

	void setLocation(FulfillmentLocation location);
}
