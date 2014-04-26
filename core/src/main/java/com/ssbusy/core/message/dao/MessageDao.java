package com.ssbusy.core.message.dao;

import java.util.List;

import org.broadleafcommerce.profile.core.domain.Customer;

import com.ssbusy.core.message.domain.Message;
import com.ssbusy.core.message.domain.MessageType;

public interface MessageDao {
	
 
    
    Message readMessageById(Long messageId); 
    
    List<Message> readMessagesForCustomer(Customer customer, MessageType messageType);
    
	List<Message> readMessagesForCustomer(Long id);
	 
	Message cretae();
	 
	void delete(Message message);

	Message send(Message message);
	
	
}
