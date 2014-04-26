package com.ssbusy.core.myorder.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.core.order.domain.MyDiscreteOrderItemImpl;
import org.broadleafcommerce.core.order.domain.OrderImpl;
import org.broadleafcommerce.core.order.domain.OrderItem;
import org.broadleafcommerce.inventory.domain.FulfillmentLocation;


@Entity
@Table(name="SB_ORDER")
public class MyOrderImpl extends OrderImpl implements MyOrder {

	private static final long serialVersionUID = 1L;
	
	@Column(name="SB_DELIEVERDATE")
	@AdminPresentation(friendlyName="ORDER_DELIVERY_DATE")
    private Date date;
	
	@Override
	public Date getDelieverDete() {
		return date;
	}

	@Override
	public void setDelieverDate(Date date) {
		this.date=date;
	}

	@Transient
	@Override
	public boolean isMultiFulfillmentLocations() {
		if (super.orderItems != null) {
			FulfillmentLocation fstLoc = null;
			for (OrderItem oi : orderItems) {
				if (oi instanceof MyDiscreteOrderItemImpl) {
					if (fstLoc == null)
						fstLoc = ((MyDiscreteOrderItemImpl) oi).getLocation();
					else {
						if (!fstLoc.equals(((MyDiscreteOrderItemImpl) oi)
								.getLocation()))
							return true;
					}
				}
			}
		}
		return false;
	}

}
