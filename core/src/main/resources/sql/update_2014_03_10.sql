insert into `blc_field` (`FIELD_ID`, `ABBREVIATION`, `ENTITY_TYPE`, `FACET_FIELD_TYPE`, `PROPERTY_NAME`, `SEARCHABLE`, `TRANSLATABLE`)
	values(11, 'invloc', 'PRODUCT', 'l', 'inventory.fulfillmentLocation.id', true, false);






-- 权限梳理
update `blc_admin_section` set `CEILING_ENTITY`='com.ssbusy.core.like.domain.CustomerLike' where `ADMIN_SECTION_ID`=22;
insert into `blc_admin_permission` (`ADMIN_PERMISSION_ID`, `DESCRIPTION`, `NAME`, `PERMISSION_TYPE`) values(84, 'Statistics', 'PERMISSION_ALL_STATISTICS', 'ALL');
insert into `blc_admin_permission_entity` (`ADMIN_PERMISSION_ENTITY_ID`, `CEILING_ENTITY`, `ADMIN_PERMISSION_ID`) values(249, 'com.ssbusy.core.like.domain.CustomerLike', 84);
update `blc_admin_sec_perm_xref` set `ADMIN_PERMISSION_ID`=84 where `ADMIN_SECTION_ID`=22 and `ADMIN_PERMISSION_ID`=79;
delete from `blc_admin_sec_perm_xref` where `ADMIN_SECTION_ID`=22 and `ADMIN_PERMISSION_ID`=80;
delete from `blc_admin_sec_perm_xref` where `ADMIN_SECTION_ID`=22 and `ADMIN_PERMISSION_ID`=81;
insert into `blc_admin_role_permission_xref` (`ADMIN_ROLE_ID`, `ADMIN_PERMISSION_ID`) values(-1, 84);

-- region:location
CREATE TABLE `ssb_region_location_xref` (`REGION_ID` bigint(20) NOT NULL,    `FULFILLMENT_LOCATION_ID` bigint(20) NOT NULL,    `fulfillmentLocations_ORDER` int(11) NOT NULL,    KEY `FK6214DB97F938BB1C` (`FULFILLMENT_LOCATION_ID`),    KEY `FK6214DB97B3D8E971` (`REGION_ID`),    CONSTRAINT `FK6214DB97B3D8E971` FOREIGN KEY (`REGION_ID`) REFERENCES `b_region` (`REGION_ID`),    CONSTRAINT `FK6214DB97F938BB1C` FOREIGN KEY (`FULFILLMENT_LOCATION_ID`) REFERENCES `blc_fulfillment_location` (`FULFILLMENT_LOCATION_ID`)  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert  into `SSB_REGION_LOCATION_XREF`(`REGION_ID`,`FULFILLMENT_LOCATION_ID`, `fulfillmentLocations_ORDER`) select REGION_ID,FULFILLMENT_LOCATION_ID,0 from b_region;

-- doi.location
CREATE TABLE `ssb_discrete_order_item` (    `ORDER_ITEM_ID` bigint(20) NOT NULL,    `LOCATION_ID` bigint(20) NOT NULL,    PRIMARY KEY  (`ORDER_ITEM_ID`),    KEY `FK4A94DCAD88F8E7FD` (`LOCATION_ID`),    KEY `FK4A94DCADB76B9466` (`ORDER_ITEM_ID`),    KEY `DISCRETE_LOC_SKU_INDEX` (`LOCATION_ID`),    CONSTRAINT `FK4A94DCAD88F8E7FD` FOREIGN KEY (`LOCATION_ID`) REFERENCES `blc_fulfillment_location` (`FULFILLMENT_LOCATION_ID`),    CONSTRAINT `FK4A94DCADB76B9466` FOREIGN KEY (`ORDER_ITEM_ID`) REFERENCES `blc_discrete_order_item` (`ORDER_ITEM_ID`)  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert into `ssb_discrete_order_item` (    `ORDER_ITEM_ID`, `LOCATION_ID`) select doi.ORDER_ITEM_ID,
     case when r.FULFILLMENT_LOCATION_ID is not null then r.FULFILLMENT_LOCATION_ID else
	(select r1.FULFILLMENT_LOCATION_ID from ssb_customer c left join b_region r1 on c.REGION_ID=r1.REGION_ID where c.CUSTOMER_ID=o.CUSTOMER_ID)
     end
 from `blc_discrete_order_item` doi left join blc_order_item oi on oi.ORDER_ITEM_ID=doi.ORDER_ITEM_ID left join blc_order o on o.order_id=oi.order_id left join blc_fulfillment_group fg on fg.ORDER_ID=o.order_id left join ssb_address ad on ad.ADDRESS_ID=fg.address_id left join ssb_dormitory d on d.DORMITORY_ID=ad.DORMITORY_ID left join ssb_area a on d.DORMITORY_AREA=a.AREA_ID left join b_region r on a.AREA_REGION=r.REGION_ID group by doi.ORDER_ITEM_ID;


-- 理工食堂3楼分仓
insert into `blc_address` (`ADDRESS_ID`, `ADDRESS_LINE1`, `ADDRESS_LINE2`, `ADDRESS_LINE3`, `CITY`, `COMPANY_NAME`, `COUNTY`, `EMAIL_ADDRESS`, `FAX`, `FIRST_NAME`, `IS_ACTIVE`, `IS_BUSINESS`, `IS_DEFAULT`, `LAST_NAME`, `POSTAL_CODE`, `PRIMARY_PHONE`, `SECONDARY_PHONE`, `STANDARDIZED`, `TOKENIZED_ADDRESS`, `VERIFICATION_LEVEL`, `ZIP_FOUR`, `COUNTRY`, `PHONE_FAX_ID`, `PHONE_PRIMARY_ID`, `PHONE_SECONDARY_ID`, `STATE_PROV_REGION`) values(-3, '浙理工3楼食堂', '', '', '杭州', '理工食堂', '', '', '', '', true, true, true, '', '310018', '', '', true, '1', '', '', 'CN', null, null, null, '浙');
insert into `blc_fulfillment_location` (`FULFILLMENT_LOCATION_ID`, `DEFAULT_LOCATION`, `LOCATION_NAME`, `PICKUP_LOCATION`, `SHIPPING_LOCATION`, `ADDRESS_ID`) values(-3, false, '理工3楼食堂', true, true, -3);
insert into `ssb_region_location_xref` (`REGION_ID`, `FULFILLMENT_LOCATION_ID`, `fulfillmentLocations_ORDER`) values(1, -3, 1);
insert into `ssb_region_location_xref` (`REGION_ID`, `FULFILLMENT_LOCATION_ID`, `fulfillmentLocations_ORDER`) values(3, -3, 1);
insert into `ssb_region_location_xref` (`REGION_ID`, `FULFILLMENT_LOCATION_ID`, `fulfillmentLocations_ORDER`) values(4, -3, 1);
insert into `ssb_region_location_xref` (`REGION_ID`, `FULFILLMENT_LOCATION_ID`, `fulfillmentLocations_ORDER`) values(5, -3, 1);
insert into `ssb_region_location_xref` (`REGION_ID`, `FULFILLMENT_LOCATION_ID`, `fulfillmentLocations_ORDER`) values(6, -3, 1);


-- location-excluded-ids
update BLC_CATEGORY_ATTRIBUTE set name='region-excluded-ids',
VALUE=(case when value=-1 then '1,3,4,5,6' else '2' end)  where name='location-excluded-ids';

-- rule exp
update blc_sc_rule set match_rule='customer.?region.?regionName=="丽水学院"' where match_rule='customer.?region.?fulfillmentLocation.?name=="丽水仓1"';
update blc_offer_rule set match_rule='customer.?region.?regionName=="丽水学院"' where match_rule='customer.?region.?fulfillmentLocation.?name=="丽水仓1"';
update blc_page_rule set match_rule='customer.?region.?regionName=="丽水学院"' where match_rule='customer.?region.?fulfillmentLocation.?name=="丽水仓1"';

update blc_sc_rule set match_rule='customer.?region.?regionName=="浙江理工大学"||customer.?region.?regionName=="福雷德生活广场"||customer.?region.?regionName=="杭州职业技术学院"||customer.?region.?regionName=="浙江传媒学院"||customer.?region.?regionName=="杭州电子科技大学"' where match_rule='customer.?region.?fulfillmentLocation.?name=="浙江理工仓1"';
update blc_offer_rule set match_rule='customer.?region.?regionName=="浙江理工大学"||customer.?region.?regionName=="福雷德生活广场"||customer.?region.?regionName=="杭州职业技术学院"||customer.?region.?regionName=="浙江传媒学院"||customer.?region.?regionName=="杭州电子科技大学"' where match_rule='customer.?region.?fulfillmentLocation.?name=="浙江理工仓1"';
update blc_page_rule set match_rule='customer.?region.?regionName=="浙江理工大学"||customer.?region.?regionName=="福雷德生活广场"||customer.?region.?regionName=="杭州职业技术学院"||customer.?region.?regionName=="浙江传媒学院"||customer.?region.?regionName=="杭州电子科技大学"' where match_rule='customer.?region.?fulfillmentLocation.?name=="浙江理工仓1"';