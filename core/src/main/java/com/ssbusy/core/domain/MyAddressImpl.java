package com.ssbusy.core.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.broadleafcommerce.common.presentation.AdminPresentationToOneLookup;
import org.broadleafcommerce.profile.core.domain.AddressImpl;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "SSB_ADDRESS")
public class MyAddressImpl extends AddressImpl implements MyAddress {
	private static final long serialVersionUID = 1L;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, targetEntity = DormitoryImpl.class, optional = true)
	@JoinColumn(name = "DORMITORY_ID")
	@AdminPresentationToOneLookup()
	private Dormitory dormitory;

	@Column(name = "ROOM_NO")
	private String roomNo = "";

	public Dormitory getDormitory() {
		return dormitory;
	}

	public void setDormitory(Dormitory dormitory) {
		this.dormitory = dormitory;
	}

	public String getRoomNo() {
		return roomNo;
	}

	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}

	@Override
	public String toString() {
		return dormitory == null ? "" : dormitory + roomNo;
	}

}
