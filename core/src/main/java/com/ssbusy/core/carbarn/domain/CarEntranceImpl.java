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

import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.AdminPresentationClass;
import org.broadleafcommerce.common.presentation.AdminPresentationToOneLookup;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 
 * @author song
 *
 */
@Entity
@Table(name="KEJUKEJI_CARENTRANCE")
@Inheritance(strategy = InheritanceType.JOINED)
@AdminPresentationClass(friendlyName = "车库入口")
public class CarEntranceImpl implements CarEntrance{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(generator = "CarEntranceId", strategy = GenerationType.TABLE)
	@GenericGenerator(name = "CarEntranceId", strategy = "org.broadleafcommerce.common.persistence.IdOverrideTableGenerator", parameters = {
			@Parameter(name = "table_name", value = "SEQUENCE_GENERATOR"),
			@Parameter(name = "segment_column_name", value = "ID_NAME"),
			@Parameter(name = "value_column_name", value = "ID_VAL"),
			@Parameter(name = "segment_value", value = "CarEntranceImpl"),
			@Parameter(name = "increment_size", value = "50"),
			@Parameter(name = "entity_name", value = "com.ssbusy.core.carbarn.domain.CarEntrance") })
	@Column(name = "CAR_ENTRANCE_ID")
	private Long id;
	@Column(name="ENTRANCE_LONGITUDE")
	@AdminPresentation(friendlyName = "经度",order = 2000,group="车库入口",groupOrder = 2000,prominent = true,gridOrder = 2)
	private Double longitude;
	@Column(name="ENTRANCE_LATITUDE")
	@AdminPresentation(friendlyName = "维度",order = 2000,group="车库入口",groupOrder = 2000,prominent = true,gridOrder = 3)
	private Double latitude;
	@ManyToOne(targetEntity = CarbarnImpl.class, optional = false)
	@JoinColumn(name = "CARBARN_ID", nullable = false)
	@AdminPresentation(friendlyName = "车库", group = "车库", prominent = true, gridOrder = 1, groupOrder = 1, order = 1)
	@AdminPresentationToOneLookup(lookupDisplayProperty = "name")
	private Carbarn carbarn;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Carbarn getCarbarn() {
		return carbarn;
	}
	public void setCarbarn(Carbarn carbarn) {
		this.carbarn = carbarn;
	}

}
