CREATE TABLE `blc_region_media_map` (
  `B_REGION_REGION_ID` bigint(20) NOT NULL,
  `MEDIA_ID` bigint(20) NOT NULL,
  `MAP_KEY` varchar(255) NOT NULL,
  PRIMARY KEY  (`B_REGION_REGION_ID`,`MAP_KEY`),
  KEY `FKD2C41CFC6E4720E0` (`MEDIA_ID`),
  KEY `FKD2C41CFC74C6DC23` (`B_REGION_REGION_ID`),
  CONSTRAINT `FKD2C41CFC6E4720E0` FOREIGN KEY (`MEDIA_ID`) REFERENCES `blc_media` (`MEDIA_ID`),
  CONSTRAINT `FKD2C41CFC74C6DC23` FOREIGN KEY (`B_REGION_REGION_ID`) REFERENCES `b_region` (`REGION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

alter table b_region add column `SHORT_NAME` varchar(255);


insert into blc_region_media_map ( B_REGION_REGION_ID, MEDIA_ID, MAP_KEY) values (1, -1, 'primary');
insert into blc_region_media_map ( B_REGION_REGION_ID, MEDIA_ID, MAP_KEY) values (2, -2, 'primary');
insert into blc_region_media_map ( B_REGION_REGION_ID, MEDIA_ID, MAP_KEY) values (3, -3, 'primary');
insert into blc_region_media_map ( B_REGION_REGION_ID, MEDIA_ID, MAP_KEY) values (4, -4, 'primary');
insert into blc_region_media_map ( B_REGION_REGION_ID, MEDIA_ID, MAP_KEY) values (5, -5, 'primary');
insert into blc_region_media_map ( B_REGION_REGION_ID, MEDIA_ID, MAP_KEY) values (6, -6, 'primary');


update b_region set short_name = "理工" where region_id = 1;
update b_region set short_name = "丽水" where region_id = 2;
update b_region set short_name = "弗雷德" where region_id = 3;
update b_region set short_name = "杭职" where region_id = 4;
update b_region set short_name = "杭电" where region_id = 5;
update b_region set short_name = "传媒" where region_id = 6;


