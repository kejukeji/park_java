package org.broadleafcommerce.openadmin.server.service.persistence.module.criteria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.broadleafcommerce.openadmin.server.dao.DynamicEntityDao;

public class CriteriaTranslatorImplEx extends CriteriaTranslatorImpl implements
		CriteriaTranslatorEx {

	@Override
	public CriteriaQuery<Serializable> translateCriteriaQuery(
			DynamicEntityDao dynamicEntityDao, String ceilingEntity,
			List<FilterMapping> filterMappings) {
		return constructCriteriaQuery(dynamicEntityDao, ceilingEntity,
				filterMappings, false);
	}

	@Override
	public CriteriaQuery<Serializable> translateCountCriteriaQuery(
			DynamicEntityDao dynamicEntityDao, String ceilingEntity,
			List<FilterMapping> filterMappings) {
		return constructCriteriaQuery(dynamicEntityDao, ceilingEntity,
				filterMappings, true);
	}

	@SuppressWarnings("unchecked")
	protected CriteriaQuery<Serializable> constructCriteriaQuery(
			DynamicEntityDao dynamicEntityDao, String ceilingEntity,
			List<FilterMapping> filterMappings, boolean isCount) {
		CriteriaBuilder criteriaBuilder = dynamicEntityDao
				.getStandardEntityManager().getCriteriaBuilder();
		Class<Serializable> ceilingMarker;
		try {
			ceilingMarker = (Class<Serializable>) Class.forName(ceilingEntity);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		Class<?>[] polyEntities = dynamicEntityDao
				.getAllPolymorphicEntitiesFromCeiling(ceilingMarker);
		Class<Serializable> ceilingClass = (Class<Serializable>) polyEntities[polyEntities.length - 1];

		CriteriaQuery<Serializable> criteria = criteriaBuilder
				.createQuery(ceilingMarker);
		Root<Serializable> original = criteria.from(ceilingClass);
		if (isCount) {
			criteria.select(criteriaBuilder.count(original));
		} else {
			criteria.select(original);
		}

		List<Predicate> restrictions = new ArrayList<Predicate>();
		List<Order> sorts = new ArrayList<Order>();
		addRestrictions(ceilingEntity, filterMappings, criteriaBuilder,
				original, restrictions, sorts);

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		if (!isCount) {
			criteria.orderBy(sorts.toArray(new Order[sorts.size()]));
		}

		return criteria;
	}

}
