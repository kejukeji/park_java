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

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "SSB_DORMITORY")
public class DormitoryImpl implements Dormitory {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DORMITORY_ID")
	private Long dormitoryId;

	@Column(name = "DORMITORY_NAME")
	private String dormitoryName;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, targetEntity = AreaAddressImpl.class, optional = true)
	@JoinColumn(name = "DORMITORY_AREA")
	@AdminPresentationToOneLookup()
	private AreaAddress areaAddress;

	public Long getDormitoryId() {
		return dormitoryId;
	}

	public void setDormitoryId(Long dormitoryId) {
		this.dormitoryId = dormitoryId;
	}

	public String getDormitoryName() {
		return dormitoryName;
	}

	public void setDormitoryName(String dormitoryName) {
		this.dormitoryName = dormitoryName;
	}

	public AreaAddress getAreaAddress() {
		return areaAddress;
	}

	public void setAreaAddress(AreaAddress areaAddress) {
		this.areaAddress = areaAddress;
	}

	@Override
	public String toString() {
		return areaAddress + dormitoryName;
	}

}
