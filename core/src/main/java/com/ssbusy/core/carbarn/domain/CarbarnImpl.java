package com.ssbusy.core.carbarn.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.AdminPresentationClass;
import org.broadleafcommerce.common.presentation.AdminPresentationCollection;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/***
 * 
 * @author song
 * 
 */
@Entity
@Table(name = "PARK_CARBARN")
@Inheritance(strategy = InheritanceType.JOINED)
@AdminPresentationClass(friendlyName = "停车场")
public class CarbarnImpl implements Carbarn {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(name = "ID")
	private Long id;
	@Column(name = "NAME")
	@AdminPresentation(friendlyName = "名字", order = 2000, group = "停车场", groupOrder = 2000, prominent = true, gridOrder = 2)
	private String name;
	@Column(name = "ADDRESS")
	@AdminPresentation(friendlyName = "地址", order = 2000, group = "停车场", groupOrder = 2000, prominent = true, gridOrder = 3)
	private String address;

	@Column(name = "DAY_PRICE")
	@AdminPresentation(friendlyName = "白天价格", order = 2000, group = "停车场", groupOrder = 2000, prominent = true, gridOrder = 4)
	private String dayPrice;

	@Column(name = "NIGHT_PRICE")
	@AdminPresentation(friendlyName = "晚上价格", order = 2000, group = "停车场", groupOrder = 2000, prominent = true, gridOrder = 5)
	private String nightPrice;

	@Column(name = "TOTAL")
	@AdminPresentation(friendlyName = "总数", order = 2000, group = "停车场", groupOrder = 2000, prominent = true, gridOrder = 6)
	private Integer total;

	@Column(name = "LAST")
	private Integer last = 0;

	@Column(name = "TYPE")
	@AdminPresentation(friendlyName = "类型", order = 2000, group = "停车场", groupOrder = 2000, prominent = true, gridOrder = 7)
	private String type;

	@Column(name = "TEL")
	@AdminPresentation(friendlyName = "电话", order = 2000, group = "停车场", groupOrder = 2000, prominent = true, gridOrder = 8)
	private String tel;

	@OneToMany(fetch = FetchType.LAZY, targetEntity = CarEntranceImpl.class, mappedBy = "carbarn")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blStandardElements")
	@BatchSize(size = 50)
	@AdminPresentationCollection(friendlyName = "入口", order = 1000, tab = "车库入口", tabOrder = 1000)
	private List<CarEntrance> cartEntrances = new ArrayList<CarEntrance>();

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getAddress() {
		return address;
	}

	@Override
	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String getDayPrice() {
		return dayPrice;
	}

	@Override
	public void setDayPrice(String dayPrice) {
		this.dayPrice = dayPrice;
	}

	@Override
	public String getNightPrice() {
		return nightPrice;
	}

	@Override
	public void setNightPrice(String nightPrice) {
		this.nightPrice = nightPrice;
	}

	@Override
	public Integer getTotal() {
		return total;
	}

	@Override
	public void setTotal(Integer total) {
		this.total = total;
	}

	@Override
	public Integer getLast() {
		return last;
	}

	@Override
	public void setLast(Integer last) {
		this.last = last;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String getTel() {
		return tel;
	}

	@Override
	public void setTel(String tel) {
		this.tel = tel;
	}

	@Override
	public List<CarEntrance> getCartEntrances() {
		return cartEntrances;
	}

	@Override
	public void setCartEntrances(List<CarEntrance> cartEntrances) {
		this.cartEntrances = cartEntrances;
	}

}
