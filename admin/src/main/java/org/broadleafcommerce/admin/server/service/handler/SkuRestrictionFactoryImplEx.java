package org.broadleafcommerce.admin.server.service.handler;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

public class SkuRestrictionFactoryImplEx extends SkuRestrictionFactoryImpl {

	@Override
	protected Predicate buildCompositePredicate(CriteriaBuilder builder,
			Path targetPropertyPath, Path productPath,
			Predicate propertyExpression, Predicate defaultSkuExpression) {
		// TODO sku.name like ? [and sku.product.defaultSku.name like ?]
		// but sku.product may null, there is join problem
		return builder.and(builder.isNotNull(targetPropertyPath),
				propertyExpression);
	}

}
