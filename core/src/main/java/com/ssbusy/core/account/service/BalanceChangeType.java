package com.ssbusy.core.account.service;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.broadleafcommerce.common.BroadleafEnumerationType;

public class BalanceChangeType implements Serializable, BroadleafEnumerationType {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Map<String, BalanceChangeType> TYPES = new LinkedHashMap<String, BalanceChangeType>();

    public static final BalanceChangeType RECHARGE = new BalanceChangeType("RECHARGE","充值");
    public static final BalanceChangeType CASHBACK = new BalanceChangeType("CASHBACK","返现");
    public static final BalanceChangeType PAYFROMBALANCE = new BalanceChangeType("PAYFROMBALANCE","余额支付");
    public static final BalanceChangeType BPBACK = new BalanceChangeType("BPBACK","退货");


    public static BalanceChangeType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public BalanceChangeType() {
        //do nothing
    }

    public BalanceChangeType(final String type, final String friendlyType) {
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
        BalanceChangeType other = (BalanceChangeType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}