package org.broadleafcommerce.core.catalog.domain;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.broadleafcommerce.inventory.domain.FulfillmentLocation;
import org.broadleafcommerce.inventory.domain.FulfillmentLocationImpl;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "SSB_PRODUCT_FEATURED")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blStandardElements")
// @AdminPresentationMergeOverrides(@AdminPresentationMergeOverride(name=""))
public class MyFeaturedProductImpl extends FeaturedProductImpl implements
		LocationAccessor {

	private static final long serialVersionUID = 1L;

	@ManyToOne(targetEntity = FulfillmentLocationImpl.class)
	@JoinColumn(name = "LOCATION_ID")
	private FulfillmentLocation location;

	@Override
	public FulfillmentLocation getLocation() {
		return location;
	}

	@Override
	public void setLocation(FulfillmentLocation location) {
		this.location = location;
	}
}
