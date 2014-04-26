package com.ssbusy.core.christmas.dao;

import java.io.Serializable;
import java.util.List;

import com.ssbusy.core.christmas.domain.ChristmasCard;

public interface ChristmasCardDao extends Serializable {

	ChristmasCard create();

	void save(ChristmasCard christmasCard);

	List<ChristmasCard> loadByCustomer(Long customerId);

}
