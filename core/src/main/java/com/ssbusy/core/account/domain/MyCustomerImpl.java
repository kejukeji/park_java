package com.ssbusy.core.account.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.broadleafcommerce.common.presentation.AdminPresentationToOneLookup;
import org.broadleafcommerce.profile.core.domain.CustomerImpl;

import com.ssbusy.core.region.domain.Region;
import com.ssbusy.core.region.domain.RegionImpl;

@Entity
@Table(name = "SSB_CUSTOMER")
public class MyCustomerImpl extends CustomerImpl implements MyCustomer {

	private static final long serialVersionUID = 1L;

	@Column(name = "AVATAR_URL")
	protected String avatarUrl;

	@ManyToOne(targetEntity = RegionImpl.class, optional = true)
	@JoinColumn(name = "REGION_ID", nullable = true)
	@AdminPresentationToOneLookup()
	protected Region region;

	@Column(name = "BALANCE", precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0.00")
	protected BigDecimal balance = BigDecimal.ZERO;

	@Column(name = "INTEGRAL", columnDefinition = "int default 0")
	protected int integral;

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Column(name = "sex")
	protected String sex;

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	@Override
	public String getAvatarUrl() {
		return avatarUrl;
	}

	@Override
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	@Override
	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

}
