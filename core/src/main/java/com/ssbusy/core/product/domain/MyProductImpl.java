package com.ssbusy.core.product.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.core.catalog.domain.ProductImpl;

@Entity
@Table(name = "SSB_PRODUCT")
public class MyProductImpl extends ProductImpl implements MyProduct {

	private static final long serialVersionUID = 1L;

	@Column(name = "TOTAL_SALED", columnDefinition = "bigint default 1")
	private Long totalSaled;

	@Column(name = "TOTAL_LIKE", columnDefinition = "bigint default 1")
	private Long totalLike;

	@Column(name = "TOTAL_JIFEN", columnDefinition = "bigint default 0")
	@AdminPresentation(friendlyName = "积分", tabOrder = Presentation.Tab.Order.Advanced, group = Presentation.Group.Name.Price, groupOrder = Presentation.Group.Order.General)
	private Long jifen;

	public Long getTotalSaled() {
		return totalSaled;
	}

	public void setTotalSaled(Long totalSaled) {
		this.totalSaled = totalSaled;
	}

	public Long getTotalLike() {
		return totalLike;
	}

	public void setTotalLike(Long totalLike) {
		this.totalLike = totalLike;
	}

	public Long getJifen() {
		return jifen;
	}

	public void setJifen(Long jifen) {
		this.jifen = jifen;
	}
}
