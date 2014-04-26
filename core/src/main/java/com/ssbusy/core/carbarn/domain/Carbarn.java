package com.ssbusy.core.carbarn.domain;

/***
 * 
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public interface Carbarn extends Serializable {
	public Long getId();

	public void setId(Long id);

	public String getName();

	public void setName(String name);

	public String getAddress();

	public void setAddress(String address);

	public BigDecimal getPrice();

	public void setPrice(BigDecimal price);

	public String getDetial();

	public void setDetial(String detial);

	public List<CarEntrance> getCartEntrances();

	public void setCartEntrances(List<CarEntrance> cartEntrances);

	public Double getCarbarnTotal();

	public void setCarbarnTotal(Double carbarnTotal);

	public Double getCarbarnLast();

	public void setCarbarnLast(Double carbarnLast);
}
