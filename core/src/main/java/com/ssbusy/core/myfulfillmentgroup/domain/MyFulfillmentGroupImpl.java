package com.ssbusy.core.myfulfillmentgroup.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.broadleafcommerce.core.order.domain.FulfillmentGroupImpl;

import com.ssbusy.core.domain.MyAddress;

@Entity
@Table(name = "B_fulfillmentGroup")
public class MyFulfillmentGroupImpl extends FulfillmentGroupImpl implements
		MyFulfillmentGroup {

	private static final long serialVersionUID = 1L;

	@Column(name = "DELIVER_DATE")
	protected Date date;

	@Override
	public Date getDelieverDate() {
		return date;
	}

	@Override
	public void setDelieverDate(Date date) {
		this.date = date;
	}

	@Override
	public MyAddress getMyAddress() {
		
		return (MyAddress)this.getAddress();
	}

}
