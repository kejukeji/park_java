alter table ssb_rechargeablecard add column `expire_date` datetime;

CREATE TABLE `sb_transation_details` (
`TRANSACTION_ID` bigint(20) NOT NULL auto_increment,
`CUSTOMER_ID` bigint(20) default NULL,
`TRANSACTION_AMOUNT` decimal(19,2) default NULL,
`TRANSACTION_TIME` datetime default NULL,
`TRANSACTION_TYPE` varchar(255) default NULL,
PRIMARY KEY  (`TRANSACTION_ID`)  )
ENGINE=InnoDB DEFAULT CHARSET=utf8;

