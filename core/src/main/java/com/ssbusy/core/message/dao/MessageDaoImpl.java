package com.ssbusy.core.message.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.broadleafcommerce.common.persistence.EntityConfiguration;
import org.broadleafcommerce.profile.core.dao.CustomerDao;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.springframework.stereotype.Repository;

import com.ssbusy.core.message.domain.Message;
import com.ssbusy.core.message.domain.MessageImpl;
import com.ssbusy.core.message.domain.MessageType;


@Repository("blMessageDao")
public class MessageDaoImpl implements MessageDao {

	
	@PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @Resource(name = "blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;
    
    @Resource(name = "blCustomerDao")
    protected CustomerDao customerDao;
    

	

	@Override
	public Message readMessageById(Long messageId) {
		  return em.find(MessageImpl.class, messageId);
	}

	@Override
	public Message send(Message message) {
		
		Message response=em.merge(message);
		return response;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Message> readMessagesForCustomer(Long customerId) {
		 final Query query = em.createNamedQuery("SSB_READ_MESSAGES_FOR_CUSTOMER");
         query.setParameter("customerId",customerId);
         return query.getResultList();
	}

	@Override
	public Message cretae() {
		  final Message message = ((Message) entityConfiguration.createEntityInstance("com.ssbusy.core.message.domain.Message"));

	        return message;
	}

	@Override
	public void delete(Message messgae) {
		 if (!em.contains(messgae)) {
			 messgae = readMessageById(messgae.getId());
	        }
	        em.remove(messgae);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Message> readMessagesForCustomer(Customer customer,MessageType messageType) {
		if(messageType==null){
			return readMessagesForCustomer(customer.getId());
		}else{
			 final Query query = em.createNamedQuery("SSB_READ_MESSAGES_BY_CUSTOMER_ID_AND_MESSAGETYPE");
	            query.setParameter("customerId", customer.getId());
	            query.setParameter("messageType", messageType.getType());
	            return query.getResultList();
		}
	
	}

}
