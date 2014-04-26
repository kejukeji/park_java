package org.broadleafcommerce.openadmin.server.service.persistence.module.criteria;

import java.io.Serializable;
import java.util.List;

import javax.persistence.criteria.CriteriaQuery;

import org.broadleafcommerce.openadmin.server.dao.DynamicEntityDao;

public interface CriteriaTranslatorEx extends CriteriaTranslator {
	CriteriaQuery<Serializable> translateCriteriaQuery(
			DynamicEntityDao dynamicEntityDao, String ceilingEntity,
			List<FilterMapping> filterMappings);

	CriteriaQuery<Serializable> translateCountCriteriaQuery(
			DynamicEntityDao dynamicEntityDao, String ceilingEntity,
			List<FilterMapping> filterMappings);

}
