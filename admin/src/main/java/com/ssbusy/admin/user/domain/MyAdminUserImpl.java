package com.ssbusy.admin.user.domain;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.AdminPresentationToOneLookup;
import org.broadleafcommerce.inventory.domain.FulfillmentLocation;
import org.broadleafcommerce.inventory.domain.FulfillmentLocationImpl;
import org.broadleafcommerce.openadmin.server.security.domain.AdminUserImpl;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "SSB_ADMINUSER")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blStandardElements")
public class MyAdminUserImpl extends AdminUserImpl implements MyAdminUser {	
	private static final long serialVersionUID = 1L;

	@ManyToOne(targetEntity = FulfillmentLocationImpl.class, optional=true)
    @JoinColumn(name = "FULFILLMENT_LOCATION_ID", referencedColumnName = "FULFILLMENT_LOCATION_ID")
	@AdminPresentation(friendlyName = "AdminUserImpl_FULFILLMENT_LOCATION_ID", order=7, group = "AdminUserImpl_User")
	@AdminPresentationToOneLookup()
	protected FulfillmentLocation  fulfillmentLocation;
     @Override
	public FulfillmentLocation getFulfillmentLocation() {
		return fulfillmentLocation;
	}

	public void setFulfillmentLocation(FulfillmentLocation fulfillmentLocation) {
		this.fulfillmentLocation = fulfillmentLocation;
	}
}
