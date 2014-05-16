package com.ssbusy.core.carbarn.domain;

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
import javax.persistence.Transient;

import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.AdminPresentationClass;
import org.broadleafcommerce.common.presentation.AdminPresentationToOneLookup;

/**
 * 
 * @author song
 * 
 */
@Entity
@Table(name = "PARK_ENTRANCE")
@Inheritance(strategy = InheritanceType.JOINED)
@AdminPresentationClass(friendlyName = "停车场入口")
public class CarEntranceImpl implements CarEntrance {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(name = "ID")
	private Long id;
	@Column(name = "LONGITUDE")
	@AdminPresentation(friendlyName = "经度", order = 2000, group = "停车场入口", groupOrder = 2000, prominent = true, gridOrder = 2)
	private Double longitude;
	@Column(name = "LATITUDE")
	@AdminPresentation(friendlyName = "维度", order = 2000, group = "停车场入口", groupOrder = 2000, prominent = true, gridOrder = 3)
	private Double latitude;
	@ManyToOne(targetEntity = CarbarnImpl.class, optional = false)
	@JoinColumn(name = "CARBARN_ID", nullable = false)
	@AdminPresentation(friendlyName = "停车场", group = "停车场", prominent = true, gridOrder = 1, groupOrder = 1, order = 1)
	@AdminPresentationToOneLookup(lookupDisplayProperty = "name")
	private Carbarn carbarn;
	@Transient
	private Double distance;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Double getLongitude() {
		return longitude;
	}

	@Override
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@Override
	public Double getLatitude() {
		return latitude;
	}

	@Override
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	@Override
	public Carbarn getCarbarn() {
		return carbarn;
	}

	@Override
	public void setCarbarn(Carbarn carbarn) {
		this.carbarn = carbarn;
	}

	@Override
	public Double getDistance() {
		return distance;
	}

	@Override
	public void setDistance(Double distance) {
		this.distance = distance;
	}

}
