package com.ssbusy.core.message.service;

import java.util.List;

import org.broadleafcommerce.profile.core.domain.Customer;

import com.ssbusy.core.message.domain.Message;
import com.ssbusy.core.message.domain.MessageType;

public interface MessageService {
 
	public List<Message>  listMessagesByCustomer(Customer customer,MessageType messageType);
	public List<Message>  listMessagesBycustomer(Long customerId);
	public void sendMessage(Message message);
	public Message find(Long messageId);
	public void deleteMessage(Message message);
	
}
