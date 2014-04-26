package com.ssbusy.core.christmas.service;

import java.io.Serializable;
import java.util.List;

import com.ssbusy.core.christmas.domain.ChristmasCard;

public interface ChristmasCardService extends Serializable{

	List<ChristmasCard> loadChristmasCard(Long customerId);
	
	ChristmasCard persist(Long customerId,int signDate); 
}
