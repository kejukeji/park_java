package com.ssbusy.core.message.domain;

import java.io.Serializable;
import java.util.Date;

import org.broadleafcommerce.profile.core.domain.Customer;

public interface Message extends Serializable {

	public Long getId();

	public void setId(Long id);

	public Customer getCustomer();

	public void setCustomer(Customer customer);

	public String getContent();

	public void setContent(String content);

	public MessageType getMessageType();

	public void setMessageType(MessageType messagetype);

	public Date getDate();

	public void setDate(Date date);
}
