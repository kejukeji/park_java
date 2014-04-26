package org.broadleafcommerce.openadmin.server.service.persistence.validation;

import java.io.Serializable;
import java.util.Map;

import org.broadleafcommerce.openadmin.dto.BasicFieldMetadata;
import org.broadleafcommerce.openadmin.dto.Entity;
import org.broadleafcommerce.openadmin.dto.FieldMetadata;

public class IntegerRangeValidator extends
		ValidationConfigurationBasedPropertyValidator {

	@Override
	public boolean validateInternal(Entity entity, Serializable instance,
			Map<String, FieldMetadata> entityFieldMetadata,
			Map<String, String> validationConfiguration,
			BasicFieldMetadata propertyMetadata, String propertyName,
			String value) {
		int val = Integer.parseInt(value);
		String max = validationConfiguration.get("max");
		String min = validationConfiguration.get("min");
		if(max != null) {
			if(val > Integer.parseInt(max))
				return false;
		}
		if(min != null) {
			if(val < Integer.parseInt(min))
				return false;
		}
		return true;
	}

}
