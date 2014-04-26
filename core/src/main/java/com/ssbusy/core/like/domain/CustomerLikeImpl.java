package com.ssbusy.core.like.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.broadleafcommerce.common.presentation.AdminPresentationToOneLookup;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.catalog.domain.ProductImpl;
@Entity
@Table(name = "SSB_CUSTOMERLIKE")
public class CustomerLikeImpl implements CustomerLike {
	private static final long serialVersionUID = 1L;
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CUSTOMERLIKE_ID")
	protected Long id;

	@Column(name = "CUSTOMER_ID")
	protected Long customerId;

	@ManyToOne(targetEntity = ProductImpl.class, optional = true)
	@JoinColumn(name = "PRODUCT_ID", nullable = true)
	@AdminPresentationToOneLookup()
	protected Product product;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	@Override
	public Product getProduct() {
		return product;
	}

	@Override
	public void setProduct(Product product) {
		this.product = product;
	}

}
