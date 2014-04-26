package org.hibernate.dialect;

public class Mysql5InnoDBBitBooleanDialect extends MySQL5InnoDBDialect {
	public Mysql5InnoDBBitBooleanDialect() {
		super();
		registerColumnType(java.sql.Types.BOOLEAN, "bit");
	}
}
