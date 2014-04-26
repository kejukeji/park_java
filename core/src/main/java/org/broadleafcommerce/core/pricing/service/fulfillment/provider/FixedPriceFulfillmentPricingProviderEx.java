package org.broadleafcommerce.core.pricing.service.fulfillment.provider;

import java.util.HashMap;
import java.util.Set;

import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.common.vendor.service.exception.FulfillmentPriceException;
import org.broadleafcommerce.core.order.domain.FulfillmentGroup;
import org.broadleafcommerce.core.order.domain.FulfillmentOption;
import org.broadleafcommerce.core.order.fulfillment.domain.FixedPriceFulfillmentOption;

public class FixedPriceFulfillmentPricingProviderEx extends
		FixedPriceFulfillmentPricingProvider {
	@Override
	public FulfillmentEstimationResponse estimateCostForFulfillmentGroup(
			FulfillmentGroup fulfillmentGroup, Set<FulfillmentOption> options)
			throws FulfillmentPriceException {

		FulfillmentEstimationResponse response = new FulfillmentEstimationResponse();
		HashMap<FulfillmentOption, Money> shippingPrices = new HashMap<FulfillmentOption, Money>();
		response.setFulfillmentOptionPrices(shippingPrices);

		for (FulfillmentOption option : options) {
			if (canCalculateCostForFulfillmentGroup(fulfillmentGroup, option)) {
				Money price = ((FixedPriceFulfillmentOption) option).getPrice();
				shippingPrices.put(option, price);
				return response;
			}
		}
		return response;
	}

}
