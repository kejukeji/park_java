/**
 * sudaw copy right 1.0 
 */
package org.broadleafcommerce.core.order.domain;

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
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * MyDiscreteOrderItemImpl.java
 * 
 * @author jamesp
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "SSB_DISCRETE_ORDER_ITEM")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "blOrderElements")
public class MyDiscreteOrderItemImpl extends DiscreteOrderItemImpl implements
		LocationedItem {

	private static final long serialVersionUID = 1L;

	@ManyToOne(targetEntity = FulfillmentLocationImpl.class, optional = false)
	@JoinColumn(name = "LOCATION_ID", nullable = false)
	@Index(name = "DISCRETE_LOC_SKU_INDEX", columnNames = { "SKU_ID",
			"LOCATION_ID" })
	@AdminPresentation(friendlyName = "DiscreteOrderItemImpl_Location", prominent = true, gridOrder = 500, order = Presentation.FieldOrder.SKU + 1, group = OrderItemImpl.Presentation.Group.Name.Catalog, groupOrder = OrderItemImpl.Presentation.Group.Order.Catalog)
	@AdminPresentationToOneLookup()
	protected FulfillmentLocation location;

	/**
	 * 本item来自哪个仓。
	 */
	@Override
	public FulfillmentLocation getLocation() {
		return location;
	}

	@Override
	public void setLocation(FulfillmentLocation location) {
		this.location = location;
	}

}
