package com.ssbusy.core.carbarn.domain;

/***
 * 
 */
import java.io.Serializable;
import java.util.List;

public interface Carbarn extends Serializable {
	public Long getId();

	public void setId(Long id);

	public String getName();

	public void setName(String name);

	public String getAddress();

	public void setAddress(String address);

	public String getDayPrice();

	public void setDayPrice(String dayPrice);

	public String getNightPrice();

	public void setNightPrice(String nightPrice);

	public Integer getTotal();

	public void setTotal(Integer total);

	public Integer getLast();

	public void setLast(Integer last);

	public String getType();

	public void setType(String type);

	public String getTel();

	public void setTel(String tel);

	public List<CarEntrance> getCartEntrances();

	public void setCartEntrances(List<CarEntrance> cartEntrances);

	
}
