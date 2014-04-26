package com.ssbusy.core.invoicing.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.broadleafcommerce.inventory.domain.Inventory;

public interface Invoicing extends Serializable {

	public Long getId();

	public void setId(Long id);

	public BigDecimal getPurchasePrice();

	public void setPurchasePrice(BigDecimal purchasePrice);

	public Integer getPurchaseQuantity();

	public void setPurchaseQuantity(Integer purchaseQuantity);

	public Date getPurchaseDate();

	public void setPurchaseDate(Date purchaseDate);

	public Inventory getInventory();

	public void setInventory(Inventory inventory);

	public BigDecimal getSalePrice();

	public void setSalePrice(BigDecimal salePrice);

	public BigDecimal getRetailPrice();

	public void setRetailPrice(BigDecimal retailPrice);
	
	public BigDecimal getMaolli();

	public void setMaolli(BigDecimal maolli);
	
}
