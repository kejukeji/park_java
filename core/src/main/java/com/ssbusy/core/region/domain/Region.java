package com.ssbusy.core.region.domain;

import java.util.List;
import java.util.Map;

import org.broadleafcommerce.common.media.domain.Media;
import org.broadleafcommerce.inventory.domain.FulfillmentLocation;

public interface Region {
	
	public Long getId();

	public void setId(Long id);

	public String getRegionName();

	public void setRegionName(String regionName);

	public List<FulfillmentLocation> getFulfillmentLocations();

	public void setFulfillmentLocations(List<FulfillmentLocation> fulfillmentLocations);
	
	public String getShipping_time();
	
	public void setShipping_time(String shipping_time);

	public Map<String, Media> getRegionMedia();

	public void setRegionMedia(Map<String, Media> regionMedia);
	
	public String getShortName();

	public void setShortName(String shortName);
	
}
