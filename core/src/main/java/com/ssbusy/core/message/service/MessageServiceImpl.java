package com.ssbusy.core.message.service;

import java.util.List;

import javax.annotation.Resource;

import org.broadleafcommerce.profile.core.domain.Customer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssbusy.core.message.dao.MessageDao;
import com.ssbusy.core.message.domain.Message;
import com.ssbusy.core.message.domain.MessageType;


@Service("MessageService")
public class MessageServiceImpl implements MessageService {

	
	  
    @Resource(name = "blMessageDao")
    protected MessageDao messageDao;
    
	


	@Override
	@Transactional("blTransactionManager")
	public void sendMessage(Message message) {
	
		messageDao.send(message);
	}

	@Override
	public Message find(Long messageId) {
		return messageDao.readMessageById(messageId);
	}

	@Override
    @Transactional("blTransactionManager")
	public void deleteMessage(Message message) {
          messageDao.delete(message);
	}

	@Override
	public List<Message> listMessagesByCustomer(Customer customer,MessageType messageType) {
		return messageDao.readMessagesForCustomer(customer, messageType);
	}

	@Override
	public List<Message> listMessagesBycustomer(Long customerId) {
		return messageDao.readMessagesForCustomer(customerId);
	}

}
