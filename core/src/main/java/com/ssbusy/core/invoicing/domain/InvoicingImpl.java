package com.ssbusy.core.invoicing.domain;

import java.math.BigDecimal;
import java.util.Date;

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
import org.broadleafcommerce.common.presentation.AdminPresentationToOneLookup;
import org.broadleafcommerce.common.presentation.client.VisibilityEnum;
import org.broadleafcommerce.inventory.domain.Inventory;
import org.broadleafcommerce.inventory.domain.InventoryImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "SSB_INVOICING")
@AdminPresentationClass(friendlyName = "进销存")
public class InvoicingImpl implements Invoicing {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "InvoicingId", strategy = GenerationType.TABLE)
	@GenericGenerator(name = "InvoicingId", strategy = "org.broadleafcommerce.common.persistence.IdOverrideTableGenerator", parameters = {
			@Parameter(name = "table_name", value = "SEQUENCE_GENERATOR"),
			@Parameter(name = "segment_column_name", value = "ID_NAME"),
			@Parameter(name = "value_column_name", value = "ID_VAL"),
			@Parameter(name = "segment_value", value = "InvoicingImpl"),
			@Parameter(name = "increment_size", value = "50"),
			@Parameter(name = "entity_name", value = "com.ssbusy.core.invoicing.domain.Invoicing") })
	@Column(name = "INVOICING_ID")
	private Long id;

	@ManyToOne(targetEntity = InventoryImpl.class, optional = false)
	@JoinColumn(name = "INVENTORY_ID", nullable = false)
	@AdminPresentation(friendlyName = "库存", group = "库存", prominent = true, gridOrder = 1, groupOrder = 1, order = 1)
	@AdminPresentationToOneLookup(lookupDisplayProperty = "sku.name")
	protected Inventory inventory;

	@Column(name = "PURCHASE_PRICE", precision = 19, scale = 5, nullable = false, updatable = false)
	@AdminPresentation(friendlyName = "进价", order = 2000, group = "进销存", groupOrder = 2000, prominent = true, gridOrder = 2)
	private BigDecimal purchasePrice;

	@Column(name = "PURCHASE_QUANTITY", nullable = false, updatable = false)
	@AdminPresentation(friendlyName = "数量", order = 3000, group = "进销存", groupOrder = 3000, prominent = true, gridOrder = 6, visibility = VisibilityEnum.VISIBLE_ALL, excluded = false)
	private Integer purchaseQuantity;

	@Column(name = "PURCHASE_DATE")
	@AdminPresentation(friendlyName = "日期", order = 4000, group = "进销存", groupOrder = 4000, prominent = true, gridOrder = 7, visibility = VisibilityEnum.VISIBLE_ALL, excluded = false)
	private Date purchaseDate;

	@Column(name = "SALE_PRICE")
	@AdminPresentation(friendlyName = "在校价", order = 5000, group = "进销存", groupOrder = 5000, prominent = true, gridOrder = 3)
	private BigDecimal salePrice;


	@Column(name="RETAIL_PRICE")
	@AdminPresentation(friendlyName = "零售价", order = 6000, group = "进销存", groupOrder = 6000, prominent = true, gridOrder = 4)
	private BigDecimal retailPrice;
	
	@Column(name="MAOLI_PRICE")
	@AdminPresentation(friendlyName = "毛利", order = 6000, group = "进销存", groupOrder = 6000, prominent = true, gridOrder = 5)
	private BigDecimal maolli;
	
	public BigDecimal getMaolli() {
		return maolli;
	}

	public void setMaolli(BigDecimal maolli) {
		this.maolli = maolli;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}
	
	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}
	
	public BigDecimal getRetailPrice() {
		return retailPrice;
	}
	
	public void setRetailPrice(BigDecimal retailPrice) {
		this.retailPrice = retailPrice;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public Integer getPurchaseQuantity() {
		return purchaseQuantity;
	}

	public void setPurchaseQuantity(Integer purchaseQuantity) {
		this.purchaseQuantity = purchaseQuantity;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public Inventory getInventory() {
		return this.inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

}
