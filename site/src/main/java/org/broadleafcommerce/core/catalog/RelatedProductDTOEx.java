package org.broadleafcommerce.core.catalog;

import org.broadleafcommerce.core.catalog.domain.RelatedProductDTO;

public class RelatedProductDTOEx extends RelatedProductDTO {
	private int start = 0;

	/**
	 * 分页支持
	 */
	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}
}
