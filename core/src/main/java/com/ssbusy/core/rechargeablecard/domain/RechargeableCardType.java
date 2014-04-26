package com.ssbusy.core.rechargeablecard.domain;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.broadleafcommerce.common.BroadleafEnumerationType;

public class RechargeableCardType implements Serializable, BroadleafEnumerationType {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Map<String, RechargeableCardType> TYPES = new LinkedHashMap<String, RechargeableCardType>();

    public static final RechargeableCardType ONCEFORCUSTOMER = new RechargeableCardType("ONCEFORCUSTOMER", "新用户一次");
    public static final RechargeableCardType NORMAL = new RechargeableCardType("NORMAL","普通");

    public static RechargeableCardType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public RechargeableCardType() {
        //do nothing
    }

    public RechargeableCardType(final String type, final String friendlyType) {
        this.friendlyType = friendlyType;
        setType(type);
    }

    public String getType() {
        return type;
    }

    public String getFriendlyType() {
        return friendlyType;
    }

    private void setType(final String type) {
        this.type = type;
        if (!TYPES.containsKey(type)) {
            TYPES.put(type, this);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RechargeableCardType other = (RechargeableCardType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}