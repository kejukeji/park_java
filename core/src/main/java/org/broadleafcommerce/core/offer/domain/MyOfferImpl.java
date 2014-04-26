package org.broadleafcommerce.core.offer.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.client.SupportedFieldType;

@Entity
@Table(name = "SSB_OFFER")
@Inheritance(strategy=InheritanceType.JOINED)
public class MyOfferImpl extends OfferImpl implements MyOffer {

	private static final long serialVersionUID = 1L;

	@Column(name = "ADD_2_BALANCE")
	@AdminPresentation(friendlyName = "MyOrderAdjustmentImpl_addToBalance", order = 5500,
            group = Presentation.Group.Name.Description, groupOrder = Presentation.Group.Order.Description,
            fieldType = SupportedFieldType.BOOLEAN)
	private Boolean addToBalance;

	@Override
	public Boolean isAddToBalance() {
		return addToBalance;
	}

	@Override
	public void setAddToBalance(Boolean addToBalance) {
		this.addToBalance = addToBalance;
	}
}
