/**
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.broadleafcommerce.inventory.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.AdminPresentationClass;
import org.broadleafcommerce.common.presentation.PopulateToOneFieldsEnum;
import org.broadleafcommerce.common.presentation.override.AdminPresentationMergeEntry;
import org.broadleafcommerce.common.presentation.override.AdminPresentationMergeOverride;
import org.broadleafcommerce.common.presentation.override.AdminPresentationMergeOverrides;
import org.broadleafcommerce.common.presentation.override.PropertyType;
import org.broadleafcommerce.profile.core.domain.Address;
import org.broadleafcommerce.profile.core.domain.AddressImpl;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "BLC_FULFILLMENT_LOCATION")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blStandardElements")
@Inheritance(strategy = InheritanceType.JOINED)
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE, friendlyName = "FulfillmentLocationImpl_baseFulfillmentLocation")
@AdminPresentationMergeOverrides({ @AdminPresentationMergeOverride(name = "address.addressLine1", mergeEntries = {
		@AdminPresentationMergeEntry(propertyType = PropertyType.AdminPresentation.FRIENDLYNAME, overrideValue = "AddressImpl_Address_1"),
		@AdminPresentationMergeEntry(propertyType = PropertyType.AdminPresentation.PROMINENT, booleanOverrideValue = true),
		@AdminPresentationMergeEntry(propertyType = PropertyType.AdminPresentation.ORDER, intOverrideValue = 7),
		@AdminPresentationMergeEntry(propertyType = PropertyType.AdminPresentation.GROUP, overrideValue = "AddressImpl_Address"),
		@AdminPresentationMergeEntry(propertyType = PropertyType.AdminPresentation.GROUPORDER, intOverrideValue = 2) }) })
public class FulfillmentLocationImpl implements FulfillmentLocation {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "FulfillmentLocationId", strategy = GenerationType.TABLE)
	@GenericGenerator(name = "FulfillmentLocationId", strategy = "org.broadleafcommerce.common.persistence.IdOverrideTableGenerator", parameters = {
			@Parameter(name = "table_name", value = "SEQUENCE_GENERATOR"),
			@Parameter(name = "segment_column_name", value = "ID_NAME"),
			@Parameter(name = "value_column_name", value = "ID_VAL"),
			@Parameter(name = "segment_value", value = "FulfillmentLocationImpl"),
			@Parameter(name = "increment_size", value = "50"),
			@Parameter(name = "entity_name", value = "org.broadleafcommerce.inventory.domain.FulfillmentLocation") })
	@Column(name = "FULFILLMENT_LOCATION_ID")
	protected Long id;

	@Column(name = "LOCATION_NAME", nullable = true)
	@AdminPresentation(friendlyName = "FulfillmentLocationImpl_name", prominent = true, group = "FulfillmentLocationImpl_generalGroupName", groupOrder = 1)
	protected String name;

	@ManyToOne(cascade = CascadeType.ALL, targetEntity = AddressImpl.class, optional = false)
	@JoinColumn(name = "ADDRESS_ID")
	protected Address address;

	@Column(name = "PICKUP_LOCATION", nullable = false)
	@AdminPresentation(friendlyName = "FulfillmentLocationImpl_pickupLocation", prominent = true, group = "FulfillmentLocationImpl_generalGroupName", groupOrder = 1)
	protected Boolean pickupLocation = Boolean.FALSE;

	@Column(name = "SHIPPING_LOCATION", nullable = false)
	@AdminPresentation(friendlyName = "FulfillmentLocationImpl_shippingLocation", prominent = true, group = "FulfillmentLocationImpl_generalGroupName", groupOrder = 1)
	protected Boolean shippingLocation = Boolean.TRUE;

	@Column(name = "DEFAULT_LOCATION", nullable = false)
	@AdminPresentation(friendlyName = "FulfillmentLocationImpl_defaultLocation", helpText = "defaultLocationHelp", prominent = true, group = "FulfillmentLocationImpl_generalGroupName", groupOrder = 1)
	protected Boolean defaultLocation = Boolean.FALSE;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Address getAddress() {
		return address;
	}

	@Override
	public void setAddress(Address address) {
		this.address = address;
	}

	@Override
	public Boolean getPickupLocation() {
		return pickupLocation;
	}

	@Override
	public void setPickupLocation(Boolean pickupLocation) {
		this.pickupLocation = pickupLocation;
	}

	@Override
	public Boolean getShippingLocation() {
		return shippingLocation;
	}

	@Override
	public void setShippingLocation(Boolean shippingLocation) {
		this.shippingLocation = shippingLocation;
	}

	@Override
	public Boolean getDefaultLocation() {
		return defaultLocation;
	}

	@Override
	public void setDefaultLocation(Boolean defaultLocation) {
		this.defaultLocation = defaultLocation;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FulfillmentLocationImpl other = (FulfillmentLocationImpl) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
