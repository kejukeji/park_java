package com.ssbusy.core.message.domain;

import java.util.Date;

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

import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.core.domain.CustomerImpl;
import org.hibernate.annotations.Index;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "SSB_MESSAGE")
public class MessageImpl implements Message {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MESSAGE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;

	@ManyToOne(targetEntity = CustomerImpl.class, optional = false)
	@JoinColumn(name = "CUSTOMER_ID", nullable = false)
	@Index(name = "ORDER_CUSTOMER_INDEX", columnNames = { "CUSTOMER_ID" })
	protected Customer customer;

	@Column(name = "MESSAGE_TYPE", nullable = false)
	protected String type;

	@Column(name = "MESSAGE_CONTENT")
	protected String content;

	@Column(name = "MESSAGE_DATE", nullable = false)
	protected Date date;

	public Long getId() {

		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Customer getCustomer() {
		return customer;
	}

	@Override
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public MessageType getMessageType() {
		return MessageType.getInstance(type);
	}

	@Override
	public void setMessageType(MessageType messagetype) {
		this.type = messagetype.getType();
	}

	@Override
	public Date getDate() {
		return date;
	}

	@Override
	public void setDate(Date date) {
		this.date = date;
	}
}
