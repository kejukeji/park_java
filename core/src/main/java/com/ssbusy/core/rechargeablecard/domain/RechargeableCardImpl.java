package com.ssbusy.core.rechargeablecard.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "ssb_rechargeablecard")
public class RechargeableCardImpl implements RechargeableCard {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "card_id")
	private String card_id;
	@Column(name = "card_password",nullable = false)
	private String card_password;
	@Column(name = "card_value",nullable = false)
	private float card_value;
	@Column(name = "CUSTOMER_ID")
	private Long id;
	@Column(name = "recharge_time")
	private Date recharge_time;
	@Column(name = "expire_date")
	private Date expireDate;
	
	@Column(name="card_type")
    protected String cardType ="NORMAL";
	
	public RechargeableCardType getCardType() {
		return RechargeableCardType.getInstance(cardType);
	}
	public void setCardType(RechargeableCardType cardType) {
		this.cardType = cardType.getType();
	}
	public Long getId() {
		return id;
	}
	public Date getRecharge_time() {
		return recharge_time;
	}
	public void setRecharge_time(Date recharge_time) {
		this.recharge_time = recharge_time;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public float getCard_value() {
		return card_value;
	}
	public void setCard_value(float card_value) {
		this.card_value = card_value;
	}
	public String getCard_id() {
		return card_id;
	}
	public  RechargeableCardImpl(){}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	public String getCard_password() {
		return card_password;
	}
	public void setCard_password(String card_password) {
		this.card_password = card_password;
	}
	public Date getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
}
