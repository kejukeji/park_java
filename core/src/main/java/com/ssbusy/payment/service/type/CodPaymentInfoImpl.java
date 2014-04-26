package com.ssbusy.payment.service.type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.broadleafcommerce.common.encryption.EncryptionModule;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "SSB_COD_PAYMENT")
public class CodPaymentInfoImpl implements CodPaymentInfo {
   
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Transient
	   protected EncryptionModule encryptionModule;
	   
	   @Id
	   @GeneratedValue(strategy=GenerationType.IDENTITY)
	   @Column(name = "COD_ID")
	   protected Long id;
	   
	    @Column(name = "REFERENCE_NUMBER", nullable=false)
	    protected String  referenceNumber;
	    
	    
	    
	   protected String message;



	@Override
	public String getReferenceNumber() {
		   return referenceNumber;
	}



	@Override
	public void setReferenceNumber(String referenceNumber) {
		 this.referenceNumber = referenceNumber;
		
	}



	@Override
	public EncryptionModule getEncryptionModule() {
		 return encryptionModule;
	}



	@Override
	public void setEncryptionModule(EncryptionModule encryptionModule) {
		   this.encryptionModule = encryptionModule;
	}



	@Override
	public String getMessage() {
		return  message;
	}



	@Override
	public void setMessage(String message) {
		this.message=message;
	}



	@Override
	public Long getId() {
		return id;

	}



	@Override
	public void setId(Long id) {
	this.id=id;
		
	}
    
}
    
    
    
