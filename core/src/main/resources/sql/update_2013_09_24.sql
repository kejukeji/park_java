CREATE TABLE `ssb_product_featured` (    `location` tinyblob,    `FEATURED_PRODUCT_ID` bigint(20) NOT NULL,    `LOCATION_ID` bigint(20) default NULL,    PRIMARY KEY  (`FEATURED_PRODUCT_ID`),    KEY `FK8920065B200575A` (`FEATURED_PRODUCT_ID`),    KEY `FK8920065B88F8E7FD` (`LOCATION_ID`),    CONSTRAINT `FK8920065B88F8E7FD` FOREIGN KEY (`LOCATION_ID`) REFERENCES `blc_fulfillment_location` (`FULFILLMENT_LOCATION_ID`),    CONSTRAINT `FK8920065B200575A` FOREIGN KEY (`FEATURED_PRODUCT_ID`) REFERENCES `blc_product_featured` (`FEATURED_PRODUCT_ID`)  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `ssb_product_up_sale` (    `UP_SALE_PRODUCT_ID` bigint(20) NOT NULL,    `LOCATION_ID` bigint(20) default NULL,    PRIMARY KEY  (`UP_SALE_PRODUCT_ID`),    KEY `FKAE34239E88F8E7FD` (`LOCATION_ID`),    KEY `FKAE34239E465BD69` (`UP_SALE_PRODUCT_ID`),    CONSTRAINT `FKAE34239E465BD69` FOREIGN KEY (`UP_SALE_PRODUCT_ID`) REFERENCES `blc_product_up_sale` (`UP_SALE_PRODUCT_ID`),    CONSTRAINT `FKAE34239E88F8E7FD` FOREIGN KEY (`LOCATION_ID`) REFERENCES `blc_fulfillment_location` (`FULFILLMENT_LOCATION_ID`)  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `ssb_product_cross_sale` (    `CROSS_SALE_PRODUCT_ID` bigint(20) NOT NULL,    `LOCATION_ID` bigint(20) default NULL,    PRIMARY KEY  (`CROSS_SALE_PRODUCT_ID`),    KEY `FKE2933FF388F8E7FD` (`LOCATION_ID`),    KEY `FKE2933FF3D7E7F9EF` (`CROSS_SALE_PRODUCT_ID`),    CONSTRAINT `FKE2933FF3D7E7F9EF` FOREIGN KEY (`CROSS_SALE_PRODUCT_ID`) REFERENCES `blc_product_cross_sale` (`CROSS_SALE_PRODUCT_ID`),    CONSTRAINT `FKE2933FF388F8E7FD` FOREIGN KEY (`LOCATION_ID`) REFERENCES `blc_fulfillment_location` (`FULFILLMENT_LOCATION_ID`)  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into ssb_product_featured(`FEATURED_PRODUCT_ID`,`location_id`) select `FEATURED_PRODUCT_ID`,-1 from blc_product_featured where `FEATURED_PRODUCT_ID` not in(select `FEATURED_PRODUCT_ID` from ssb_product_featured);
insert into SSB_PRODUCT_UP_SALE(`UP_SALE_PRODUCT_ID`,`location_id`) select `UP_SALE_PRODUCT_ID`,-1 from blc_product_UP_SALE where `UP_SALE_PRODUCT_ID` not in(select `UP_SALE_PRODUCT_ID` from SSB_PRODUCT_UP_SALE);
insert into ssb_product_CROSS_SALE(`CROSS_SALE_PRODUCT_ID`,`location_id`) select `CROSS_SALE_PRODUCT_ID`,-1 from blc_product_CROSS_SALE where `CROSS_SALE_PRODUCT_ID` not in(select `CROSS_SALE_PRODUCT_ID` from ssb_product_CROSS_SALE);

insert into blc_admin_permission_entity(ADMIN_PERMISSION_ENTITY_ID, CEILING_ENTITY, ADMIN_PERMISSION_ID) values(-1534, 'org.broadleafcommerce.core.catalog.domain.MyCrossSaleProductImpl', -46);
insert into blc_admin_permission_entity(ADMIN_PERMISSION_ENTITY_ID, CEILING_ENTITY, ADMIN_PERMISSION_ID) values(-1533, 'org.broadleafcommerce.core.catalog.domain.MyCrossSaleProductImpl', -45);
insert into blc_admin_permission_entity(ADMIN_PERMISSION_ENTITY_ID, CEILING_ENTITY, ADMIN_PERMISSION_ID) values(-1532, 'org.broadleafcommerce.core.catalog.domain.MyCrossSaleProductImpl', -44);
insert into blc_admin_permission_entity(ADMIN_PERMISSION_ENTITY_ID, CEILING_ENTITY, ADMIN_PERMISSION_ID) values(-1531, 'org.broadleafcommerce.core.catalog.domain.MyCrossSaleProductImpl', -43);
insert into blc_admin_permission_entity(ADMIN_PERMISSION_ENTITY_ID, CEILING_ENTITY, ADMIN_PERMISSION_ID) values(-1530, 'org.broadleafcommerce.core.catalog.domain.MyCrossSaleProductImpl', -42);
insert into blc_admin_permission_entity(ADMIN_PERMISSION_ENTITY_ID, CEILING_ENTITY, ADMIN_PERMISSION_ID) values(-1114, 'org.broadleafcommerce.core.catalog.domain.MyUpSaleProductImpl   ', -11);
insert into blc_admin_permission_entity(ADMIN_PERMISSION_ENTITY_ID, CEILING_ENTITY, ADMIN_PERMISSION_ID) values(-1113, 'org.broadleafcommerce.core.catalog.domain.MyUpSaleProductImpl   ', -10);
insert into blc_admin_permission_entity(ADMIN_PERMISSION_ENTITY_ID, CEILING_ENTITY, ADMIN_PERMISSION_ID) values(-1112, 'org.broadleafcommerce.core.catalog.domain.MyUpSaleProductImpl   ',  -9);
insert into blc_admin_permission_entity(ADMIN_PERMISSION_ENTITY_ID, CEILING_ENTITY, ADMIN_PERMISSION_ID) values(-1111, 'org.broadleafcommerce.core.catalog.domain.MyUpSaleProductImpl   ',  -8);
insert into blc_admin_permission_entity(ADMIN_PERMISSION_ENTITY_ID, CEILING_ENTITY, ADMIN_PERMISSION_ID) values(-1110, 'org.broadleafcommerce.core.catalog.domain.MyUpSaleProductImpl   ',  -7);
insert into blc_admin_permission_entity(ADMIN_PERMISSION_ENTITY_ID, CEILING_ENTITY, ADMIN_PERMISSION_ID) values(-137 , 'org.broadleafcommerce.core.catalog.domain.MyUpSaleProductImpl   ',  -6);
insert into blc_admin_permission_entity(ADMIN_PERMISSION_ENTITY_ID, CEILING_ENTITY, ADMIN_PERMISSION_ID) values(-136 , 'org.broadleafcommerce.core.catalog.domain.MyUpSaleProductImpl   ',  -5);
insert into blc_admin_permission_entity(ADMIN_PERMISSION_ENTITY_ID, CEILING_ENTITY, ADMIN_PERMISSION_ID) values(-135 , 'org.broadleafcommerce.core.catalog.domain.MyUpSaleProductImpl   ',  -4);
insert into blc_admin_permission_entity(ADMIN_PERMISSION_ENTITY_ID, CEILING_ENTITY, ADMIN_PERMISSION_ID) values(-134 , 'org.broadleafcommerce.core.catalog.domain.MyUpSaleProductImpl   ',  -3);
insert into blc_admin_permission_entity(ADMIN_PERMISSION_ENTITY_ID, CEILING_ENTITY, ADMIN_PERMISSION_ID) values(-133 , 'org.broadleafcommerce.core.catalog.domain.MyUpSaleProductImpl   ',  -2);
insert into blc_admin_permission_entity(ADMIN_PERMISSION_ENTITY_ID, CEILING_ENTITY, ADMIN_PERMISSION_ID) values(-132 , 'org.broadleafcommerce.core.catalog.domain.MyCrossSaleProductImpl',  -6);
insert into blc_admin_permission_entity(ADMIN_PERMISSION_ENTITY_ID, CEILING_ENTITY, ADMIN_PERMISSION_ID) values(-131 , 'org.broadleafcommerce.core.catalog.domain.MyCrossSaleProductImpl',  -5);
insert into blc_admin_permission_entity(ADMIN_PERMISSION_ENTITY_ID, CEILING_ENTITY, ADMIN_PERMISSION_ID) values(-130 , 'org.broadleafcommerce.core.catalog.domain.MyCrossSaleProductImpl',  -4);
insert into blc_admin_permission_entity(ADMIN_PERMISSION_ENTITY_ID, CEILING_ENTITY, ADMIN_PERMISSION_ID) values(-129 , 'org.broadleafcommerce.core.catalog.domain.MyCrossSaleProductImpl',  -3);
insert into blc_admin_permission_entity(ADMIN_PERMISSION_ENTITY_ID, CEILING_ENTITY, ADMIN_PERMISSION_ID) values(-128 , 'org.broadleafcommerce.core.catalog.domain.MyCrossSaleProductImpl',  -2);
insert into blc_admin_permission_entity(ADMIN_PERMISSION_ENTITY_ID, CEILING_ENTITY, ADMIN_PERMISSION_ID) values(-127 , 'org.broadleafcommerce.core.catalog.domain.MyFeaturedProductImpl ',  -6);
insert into blc_admin_permission_entity(ADMIN_PERMISSION_ENTITY_ID, CEILING_ENTITY, ADMIN_PERMISSION_ID) values(-126 , 'org.broadleafcommerce.core.catalog.domain.MyFeaturedProductImpl ',  -5);
insert into blc_admin_permission_entity(ADMIN_PERMISSION_ENTITY_ID, CEILING_ENTITY, ADMIN_PERMISSION_ID) values(-125 , 'org.broadleafcommerce.core.catalog.domain.MyFeaturedProductImpl ',  -4);
insert into blc_admin_permission_entity(ADMIN_PERMISSION_ENTITY_ID, CEILING_ENTITY, ADMIN_PERMISSION_ID) values(-124 , 'org.broadleafcommerce.core.catalog.domain.MyFeaturedProductImpl ',  -3);
insert into blc_admin_permission_entity(ADMIN_PERMISSION_ENTITY_ID, CEILING_ENTITY, ADMIN_PERMISSION_ID) values(-123 , 'org.broadleafcommerce.core.catalog.domain.MyFeaturedProductImpl ',  -2 );


alter table b_region add column `SHIPPING_TIME` varchar(255);

update b_region set SHIPPING_TIME='09:00-21:45' where REGION_ID=1;
update b_region set SHIPPING_TIME='09:00-22:10' where REGION_ID=2;
update b_region set SHIPPING_TIME='09:00-21:45' where REGION_ID=3;
update b_region set SHIPPING_TIME='09:00-21:45' where REGION_ID=4;
update b_region set SHIPPING_TIME='09:00-21:45' where REGION_ID=5;
update b_region set SHIPPING_TIME='09:00-21:45' where REGION_ID=6;

alter table ssb_rechargeablecard add column `card_type` varchar(255);
update ssb_rechargeablecard set card_type ='NORMAL';

alter table ssb_product add column `total_jifen` long;

