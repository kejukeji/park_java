/**
 * sudaw copy right 1.0 
 */
package org.broadleafcommerce.openadmin.server.service.persistence.module.criteria.converter;

/**
 * 
 * 
 * StringLikeFilterValueConverterEx.java
 * 
 * @author jamesp
 */
public class StringLikeFilterValueConverterEx implements FilterValueConverter<String> {

    @Override
    public String convert(String stringValue) {
        return "%" + stringValue.toLowerCase() + "%";
    }

}
