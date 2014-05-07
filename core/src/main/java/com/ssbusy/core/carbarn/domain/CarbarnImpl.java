package com.ssbusy.core.carbarn.domain;

import java.math.BigDecimal;
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
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/***
 * 
 * @author song
 * 
 */
@Entity
@Table(name = "KEJUKEJI_CARBARN")
@Inheritance(strategy = InheritanceType.JOINED)
@AdminPresentationClass(friendlyName = "车库")
public class CarbarnImpl implements Carbarn {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "CarbarnId", strategy = GenerationType.TABLE)
	@GenericGenerator(name = "CarbarnId", strategy = "org.broadleafcommerce.common.persistence.IdOverrideTableGenerator", parameters = {
			@Parameter(name = "table_name", value = "SEQUENCE_GENERATOR"),
			@Parameter(name = "segment_column_name", value = "ID_NAME"),
			@Parameter(name = "value_column_name", value = "ID_VAL"),
			@Parameter(name = "segment_value", value = "CarbarnImpl"),
			@Parameter(name = "increment_size", value = "50"),
			@Parameter(name = "entity_name", value = "com.ssbusy.core.carbarn.domain.Carbarn") })
	@Column(name = "CARBARN_ID")
	private Long id;
	@Column(name = "CARBARN_NAME")
	@AdminPresentation(friendlyName = "车库名字", order = 2000, group = "车库", groupOrder = 2000, prominent = true, gridOrder = 2)
	private String name;
	@Column(name = "CARBARN_ADDRESS", nullable = false)
	@AdminPresentation(friendlyName = "车库地址", order = 2000, group = "车库", groupOrder = 2000, prominent = true, gridOrder = 3)
	private String address;
	@Column(name = "CARBARN_PRICE", nullable = false)
	@AdminPresentation(friendlyName = "价格(/小时)", order = 2000, group = "车库", groupOrder = 2000, prominent = true, gridOrder = 4)
	private BigDecimal price;

	@Column(name = "CARBARN_DETIAL")
	@AdminPresentation(friendlyName = "备注", order = 2000, group = "车库", groupOrder = 2000, prominent = true, gridOrder = 6)
	private String detial;

	@OneToMany(fetch = FetchType.LAZY, targetEntity = CarEntranceImpl.class, mappedBy = "carbarn")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blStandardElements")
	@BatchSize(size = 50)
	@AdminPresentationCollection(friendlyName = "入口", order = 1000, tab = "车库入口", tabOrder = 1000)
	private List<CarEntrance> cartEntrances = new ArrayList<CarEntrance>();

	@Column(name = "CARBARN_TOTAL", nullable = false)
	@AdminPresentation(friendlyName = "车位总数", order = 2000, group = "车库", groupOrder = 2000, prominent = true, gridOrder = 5)
	private Integer carbarnTotal;

	@Column(name = "CARBARN_LAST", nullable = false)
	private Integer carbarnLast = 0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getDetial() {
		return detial;
	}

	public void setDetial(String detial) {
		this.detial = detial;
	}

	public List<CarEntrance> getCartEntrances() {
		return cartEntrances;
	}

	public void setCartEntrances(List<CarEntrance> cartEntrances) {
		this.cartEntrances = cartEntrances;
	}

	public Integer getCarbarnTotal() {
		return carbarnTotal;
	}

	public void setCarbarnTotal(Integer carbarnTotal) {
		this.carbarnTotal = carbarnTotal;
	}

	public Integer getCarbarnLast() {
		return carbarnLast;
	}

	public void setCarbarnLast(Integer carbarnLast) {
		this.carbarnLast = carbarnLast;
	}

	

}
