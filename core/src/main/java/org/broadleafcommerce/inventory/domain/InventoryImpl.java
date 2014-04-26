/**
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.broadleafcommerce.inventory.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.AdminPresentationClass;
import org.broadleafcommerce.common.presentation.AdminPresentationCollection;
import org.broadleafcommerce.common.presentation.AdminPresentationToOneLookup;
import org.broadleafcommerce.common.presentation.ConfigurationItem;
import org.broadleafcommerce.common.presentation.PopulateToOneFieldsEnum;
import org.broadleafcommerce.common.presentation.ValidationConfiguration;
import org.broadleafcommerce.common.presentation.client.SupportedFieldType;
import org.broadleafcommerce.common.presentation.override.AdminPresentationMergeEntry;
import org.broadleafcommerce.common.presentation.override.AdminPresentationMergeOverride;
import org.broadleafcommerce.common.presentation.override.AdminPresentationMergeOverrides;
import org.broadleafcommerce.common.presentation.override.PropertyType;
import org.broadleafcommerce.core.catalog.domain.Sku;
import org.broadleafcommerce.core.catalog.domain.SkuImpl;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.ssbusy.core.invoicing.domain.Invoicing;
import com.ssbusy.core.invoicing.domain.InvoicingImpl;

@Entity
@Table(name = "BLC_INVENTORY", uniqueConstraints = {@UniqueConstraint(columnNames = {"FULFILLMENT_LOCATION_ID", "SKU_ID"})})
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blInventoryElements")
@Inheritance(strategy = InheritanceType.JOINED)
@AdminPresentationMergeOverrides(
	{
		@AdminPresentationMergeOverride(name = "sku.id", mergeEntries = {@AdminPresentationMergeEntry(propertyType=PropertyType.AdminPresentation.FRIENDLYNAME, overrideValue = "InventoryImpl_skuId"),
																		@AdminPresentationMergeEntry(propertyType=PropertyType.AdminPresentation.EXCLUDED, booleanOverrideValue = false),
																		@AdminPresentationMergeEntry(propertyType=PropertyType.AdminPresentation.PROMINENT, booleanOverrideValue = true),
																		@AdminPresentationMergeEntry(propertyType=PropertyType.AdminPresentation.ORDER, intOverrideValue = 1)}),
		@AdminPresentationMergeOverride(name = "sku.name", mergeEntries = {@AdminPresentationMergeEntry(propertyType=PropertyType.AdminPresentation.FRIENDLYNAME, overrideValue ="InventoryImpl_skuName"),
				@AdminPresentationMergeEntry(propertyType=PropertyType.AdminPresentation.EXCLUDED, booleanOverrideValue = false),
				@AdminPresentationMergeEntry(propertyType=PropertyType.AdminPresentation.PROMINENT, booleanOverrideValue = true),
				@AdminPresentationMergeEntry(propertyType=PropertyType.AdminPresentation.ORDER, intOverrideValue = 2),
				@AdminPresentationMergeEntry(propertyType=PropertyType.AdminPresentation.VISIBILITY,  overrideValue= "FORM_HIDDEN")}),
		// These properties are declared as @AdminPresentationMergeOverrides in either fulfillmentLocation or address, so we need
		// to ensure they are excluded in Inventory's list of overrides
		@AdminPresentationMergeOverride(name = "fulfillmentLocation.address.addressLine1", mergeEntries = @AdminPresentationMergeEntry(propertyType=PropertyType.AdminPresentation.EXCLUDED, booleanOverrideValue = true)),
		@AdminPresentationMergeOverride(name = "fulfillmentLocation.address.phonePrimary.phoneNumber", mergeEntries = @AdminPresentationMergeEntry(propertyType=PropertyType.AdminPresentation.EXCLUDED, booleanOverrideValue = true)),
		@AdminPresentationMergeOverride(name = "fulfillmentLocation.address.phoneSecondary.phoneNumber", mergeEntries = @AdminPresentationMergeEntry(propertyType=PropertyType.AdminPresentation.EXCLUDED, booleanOverrideValue = true)),
		@AdminPresentationMergeOverride(name = "fulfillmentLocation.address.phoneFax.phoneNumber", mergeEntries = @AdminPresentationMergeEntry(propertyType=PropertyType.AdminPresentation.EXCLUDED, booleanOverrideValue = true))
	}
)
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE, friendlyName = "InventoryImpl_baseInventory")
public class InventoryImpl implements Inventory {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "InventoryId", strategy = GenerationType.TABLE)
	@GenericGenerator(name = "InventoryId", strategy = "org.broadleafcommerce.common.persistence.IdOverrideTableGenerator", parameters = {
			@Parameter(name = "table_name", value = "SEQUENCE_GENERATOR"),
			@Parameter(name = "segment_column_name", value = "ID_NAME"),
			@Parameter(name = "value_column_name", value = "ID_VAL"),
			@Parameter(name = "segment_value", value = "InventoryImpl"),
			@Parameter(name = "increment_size", value = "50"),
			@Parameter(name = "entity_name", value = "org.broadleafcommerce.inventory.domain.Inventory") })
	@Column(name = "INVENTORY_ID")
	protected Long id;

	@ManyToOne(targetEntity = FulfillmentLocationImpl.class, optional = false)
	@JoinColumn(name = "FULFILLMENT_LOCATION_ID", nullable = false)
	// @AdminPresentation(excluded=true, visibility = VisibilityEnum.HIDDEN_ALL)
	@AdminPresentation(friendlyName="InventoryImpl_fulfillmentLocation", group = "Sku", groupOrder = 1, order = 2)
	@AdminPresentationToOneLookup
	protected FulfillmentLocation fulfillmentLocation;

	@ManyToOne(targetEntity = SkuImpl.class, optional = false)
	@JoinColumn(name = "SKU_ID", nullable = false)
	@AdminPresentation(friendlyName="Sku", group = "Sku", groupOrder = 1, order = 1)
	@AdminPresentationToOneLookup(customCriteria = { "inventoryFilteredSkuList" })
	protected Sku sku;

	@Column(name = "QUANTITY_AVAILABLE", nullable = false)
	@AdminPresentation(friendlyName = "InventoryImpl_quantityAvailable", prominent = true, group = "Quantities",
			groupOrder = 2, order = 4, fieldType = SupportedFieldType.INTEGER, helpText = "quantityAvailableHelp",
			validationConfigurations = {
					@ValidationConfiguration(
							validationImplementation="org.broadleafcommerce.openadmin.server.service.persistence.validation.IntegerRangeValidator",
							configurationItems={
									@ConfigurationItem(itemName="min", itemValue="0")
							}
					)
			})
	protected Integer quantityAvailable;

    @OneToMany(fetch = FetchType.LAZY, targetEntity = InvoicingImpl.class, mappedBy="inventory")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blStandardElements")
    @BatchSize(size = 50)
    @AdminPresentationCollection(friendlyName="进销存", order = 1000,
      tab = "进销存", tabOrder = 1000)
    protected List<Invoicing> invoicings = new ArrayList<Invoicing>();

	@Column(name = "QUANTITY_ON_HAND", nullable = false)
	@AdminPresentation(friendlyName = "InventoryImpl_quantityOnHand", prominent = true, group = "Quantities",
			groupOrder = 2, order = 5, helpText = "quantityOnHandHelp",
			validationConfigurations = {
					@ValidationConfiguration(
							validationImplementation="org.broadleafcommerce.openadmin.server.service.persistence.validation.IntegerRangeValidator",
							configurationItems={
									@ConfigurationItem(itemName="min", itemValue="0")
							}
					)
			})
	protected Integer quantityOnHand;


	@Column(name = "QUANTITY_SAFE")
	@AdminPresentation(friendlyName = "InventoryImpl_quantitySafe", prominent = true, group = "Quantities",
	groupOrder = 2, order = 5, helpText = "quantitySafeHelp",
	validationConfigurations = {
			@ValidationConfiguration(
					validationImplementation="org.broadleafcommerce.openadmin.server.service.persistence.validation.IntegerRangeValidator",
					configurationItems={
							@ConfigurationItem(itemName="min", itemValue="0")
					}
					)
	})
	protected Integer quantitySafe = 0;

	@Formula("QUANTITY_AVAILABLE-QUANTITY_SAFE")
	protected Integer quantitySafeDiff;

	@Column(name = "EXPECTED_AVAILABILITY_DATE")
	protected Date expectedAvailabilityDate;

	@Version
	@Column(name = "VERSION_NUM", nullable = false)
	@AdminPresentation(excluded = true)
	protected Long version;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public FulfillmentLocation getFulfillmentLocation() {
		return fulfillmentLocation;
	}

	@Override
	public void setFulfillmentLocation(FulfillmentLocation fulfillmentLocation) {
		this.fulfillmentLocation = fulfillmentLocation;
	}

	@Override
	public Sku getSku() {
		return sku;
	}

	@Override
	public void setSku(Sku sku) {
		this.sku = sku;
	}

	@Override
	public Integer getQuantityAvailable() {
		return quantityAvailable;
	}

	@Override
	public void setQuantityAvailable(Integer quantityAvailable) {
		this.quantityAvailable = quantityAvailable;
	}

	@Override
	public Integer getQuantityOnHand() {
		return quantityOnHand;
	}

	@Override
	public void setQuantityOnHand(Integer quantityOnHand) {
		this.quantityOnHand = quantityOnHand;
	}

	public List<Invoicing> getInvoicings() {
	        return invoicings;
	}
	
	public void setInvoicings(List<Invoicing> invoicings) {
		this.invoicings = invoicings;
	}
	
	@Override
	public Integer getQuantitySafe() {
		return quantitySafe;
	}

	@Override
	public void setQuantitySafe(Integer quantitySafe) {
		this.quantitySafe = quantitySafe;
	}

	public Integer getQuantitySafeDiff() {
		return quantitySafeDiff;
	}

	public void setQuantitySafeDiff(Integer quantitySafeDiff) {
		this.quantitySafeDiff = quantitySafeDiff;
	}

	@Override
	public Date getExpectedAvailabilityDate() {
		return expectedAvailabilityDate;
	}

	@Override
	public void setExpectedAvailabilityDate(Date expectedAvailabilityDate) {
		this.expectedAvailabilityDate = expectedAvailabilityDate;
	}

	@Override
	public Long getVersion() {
		return version;
	}
}
