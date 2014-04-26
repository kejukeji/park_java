package com.ssbusy.core.domain;

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

import org.broadleafcommerce.common.presentation.AdminPresentationToOneLookup;

import com.ssbusy.core.region.domain.Region;
import com.ssbusy.core.region.domain.RegionImpl;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "SSB_AREA")
public class AreaAddressImpl implements AreaAddress {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AREA_ID")
	private Long areaId;

	@Column(name = "AREA_NAME")
	private String areaName;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, targetEntity = RegionImpl.class, optional = true)
	@JoinColumn(name = "AREA_REGION")
	@AdminPresentationToOneLookup()
	private Region region;

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	@Override
	public String toString() {
		return region + areaName;
	}
}
