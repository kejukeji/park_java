package com.ssbusy.core.message.domain;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.broadleafcommerce.common.BroadleafEnumerationType;

public class MessageType implements Serializable, BroadleafEnumerationType {
   
	   private static final long serialVersionUID = 1L;
	   private static final Map<String, MessageType> TYPES = new LinkedHashMap<String, MessageType>();
	   public static final MessageType REMIND= new MessageType("RENIND","Remind");
	   public static final MessageType NOTICE= new MessageType("NOTICE","Notice");
	   private String type;
	   private String friendlyType;

	   public MessageType(){
		   
	   }
	   public MessageType(final String type, final String friendlyType) {
		 this.friendlyType = friendlyType;
	        setType(type);
	   }
 
	   public static MessageType getInstance(final String type) {
	        return TYPES.get(type);
	    }

     	@Override
	   public String getType() {
		return type;
	  }
	
	  private void setType(final String type) {
	        this.type = type;
	        if (!TYPES.containsKey(type)) {
	            TYPES.put(type, this);
	        }
	    }

	@Override
	public String getFriendlyType() {
		return friendlyType;
	}
	
	  public int hashCode() {
	        final int prime = 31;
	        int result = 1;
	        result = prime * result + ((type == null) ? 0 : type.hashCode());
	        return result;
	    }

	    @Override
	    public boolean equals(Object obj) {
	        if (this == obj)
	            return true;
	        if (obj == null)
	            return false;
	        if (getClass() != obj.getClass())
	            return false;
	        MessageType other = (MessageType) obj;
	        if (type == null) {
	            if (other.type != null)
	                return false;
	        } else if (!type.equals(other.type))
	            return false;
	        return true;
	    }

	
}
