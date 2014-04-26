package com.ssbusy.admin.user.domain;

import org.broadleafcommerce.inventory.domain.FulfillmentLocation;
import org.broadleafcommerce.openadmin.server.security.domain.AdminUser;

public interface MyAdminUser extends AdminUser{
	public FulfillmentLocation getFulfillmentLocation();
    public void setFulfillmentLocation(FulfillmentLocation fulfillmentLocation);
}
