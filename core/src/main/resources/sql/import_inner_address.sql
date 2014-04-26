insert into b_region(REGION_ID, REGION_NAME, FULFILLMENT_LOCATION_ID) values(1, '浙江理工大学', -1), (2, '丽水学院', -2), (3, '福雷德生活广场', -1);

insert into b_region(REGION_ID, REGION_NAME, FULFILLMENT_LOCATION_ID) values(4, '杭州职业技术学院', -1);
insert into b_region(REGION_ID, REGION_NAME, FULFILLMENT_LOCATION_ID) values(5, '杭州电子科技大学', -1);
insert into b_region(REGION_ID, REGION_NAME, FULFILLMENT_LOCATION_ID) values(6, '浙江传媒学院', -1);

-- 浙江理工
insert into ssb_area(AREA_ID, area_name,area_region)values(1, '生活一区',1);
insert into ssb_area(AREA_ID, area_name,area_region)values(2, '生活二区',1);
insert into ssb_area(AREA_ID, area_name,area_region)values(3, '生活三区',1);
-- insert into ssb_area(AREA_ID, area_name,area_region)values(4, "教学区",1);
insert into ssb_area(AREA_ID, area_name,area_region)values(9, '其他',1);
insert into ssb_area(AREA_ID, area_name,area_region)values(19, '其他',2);
insert into ssb_area(AREA_ID, area_name,area_region)values(29, '其他',3);
insert into ssb_area(AREA_ID, area_name,area_region)values(39, '其他',4);
insert into ssb_area(AREA_ID, area_name,area_region)values(49, '其他',5);
insert into ssb_area(AREA_ID, area_name,area_region)values(59, '其他',6);
insert into ssb_dormitory(DORMITORY_ID,dormitory_name,dormitory_area)values(-1,'其他',9);
insert into ssb_dormitory(DORMITORY_ID,dormitory_name,dormitory_area)values(-2,'其他',19);
insert into ssb_dormitory(DORMITORY_ID,dormitory_name,dormitory_area)values(-3,'其他',29);
insert into ssb_dormitory(DORMITORY_ID,dormitory_name,dormitory_area)values(-4,'其他',39);
insert into ssb_dormitory(DORMITORY_ID,dormitory_name,dormitory_area)values(-5,'其他',49);
insert into ssb_dormitory(DORMITORY_ID,dormitory_name,dormitory_area)values(-6,'其他',59);

insert into ssb_dormitory(dormitory_name,dormitory_area)values('1号楼',1);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('2号楼',1);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('3号南楼',1);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('3号北楼',1);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('4号南楼',1);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('4号北楼',1);


insert into ssb_dormitory(dormitory_name,dormitory_area)values('2号东楼',2);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('2号西楼',2);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('4号南楼',2);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('4号北楼',2);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('5号南楼',2);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('5号北楼',2);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('6号南楼',2);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('6号北楼',2);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('12号楼',2);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('研究生楼',2);


insert into ssb_dormitory(dormitory_name,dormitory_area)values('1号东楼',3);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('1号西楼',3);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('3号东楼',3);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('3号西楼',3);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('7号楼',3);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('8号楼',3);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('9号楼',3);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('10号楼',3);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('11号楼',3);




-- insert into ssb_dormitory(dormitory_name,dormitory_area)values("2号楼",4);
-- insert into ssb_dormitory(dormitory_name,dormitory_area)values("3号楼",4);
-- insert into ssb_dormitory(dormitory_name,dormitory_area)values("6号楼",4);
-- insert into ssb_dormitory(dormitory_name,dormitory_area)values("7号楼",4);
-- insert into ssb_dormitory(dormitory_name,dormitory_area)values("10号楼",4);
-- insert into ssb_dormitory(dormitory_name,dormitory_area)values("17号楼",4);
-- insert into ssb_dormitory(dormitory_name,dormitory_area)values("18号楼",4);

-- 丽水学院
insert into ssb_area(AREA_ID, area_name,area_region)values(11, '生活西区',2);
insert into ssb_area(AREA_ID, area_name,area_region)values(12, '生活东区',2);
insert into ssb_area(AREA_ID, area_name,area_region)values(13, '教学区西区',2);
insert into ssb_area(AREA_ID, area_name,area_region)values(14, '教学区东区',2);


insert into ssb_dormitory(dormitory_name,dormitory_area)values('4号楼',11);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('5号楼',11);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('6号楼',11);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('7号楼',11);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('8号楼',11);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('9号楼',11);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('10号楼',11);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('11号楼',11);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('12号楼',11);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('13号楼',11);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('14号楼',11);

insert into ssb_dormitory(dormitory_name,dormitory_area)values('3号楼',12);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('7号楼',12);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('8号楼',12);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('9号楼',12);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('10号楼',12);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('15号楼',12);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('1号楼',12);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('2号楼',12);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('4号楼',12);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('5号楼',12);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('6号楼',12);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('11号楼',12);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('12号楼',12);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('13号楼',12);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('14号楼',12);


insert into ssb_dormitory(dormitory_name,dormitory_area)values('1号楼',13);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('2号楼',13);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('3号楼',13);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('4号楼',13);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('5号楼',13);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('6号楼',13);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('7号楼',13);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('8号楼',13);



insert into ssb_dormitory(dormitory_name,dormitory_area)values('9号楼',14);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('10号楼',14);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('11号楼',14);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('12号楼',14);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('13号楼',14);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('14号楼',14);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('15号楼',14);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('16号楼',14);



-- 福瑞德
insert into ssb_area(AREA_ID, area_name,area_region)values(21, '艾肯金座',3);

insert into ssb_dormitory(dormitory_name,dormitory_area)values('艾肯金座',21);

--杭州职业技术学院
insert into ssb_area(AREA_ID, area_name,area_region)values(31, '含晖苑',4);

insert into ssb_dormitory(dormitory_name,dormitory_area)values('5号南楼',31);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('5号北楼',31);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('6号南楼',31);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('6号北楼',31);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('7号楼',31);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('8号楼',31);

--杭州电子科技大学
insert into ssb_area(AREA_ID, area_name,area_region)values(41, '杭电生活区',5);

insert into ssb_dormitory(dormitory_name,dormitory_area)values('2号楼',41);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('3号楼',41);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('4号南楼',41);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('4号北楼',41);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('5号南楼',41);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('5号北楼',41);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('6号南楼',41);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('8号楼',41);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('10号南楼',41);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('10号北楼',41);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('11号南楼',41);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('11号北楼',41);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('12号南楼',41);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('12号北楼',41);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('13号楼',41);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('14号楼',41);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('15号楼',41);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('16号楼',41);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('18号楼',41);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('21号楼',41);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('22号楼',41);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('27号楼',41);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('28号楼',41);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('29号楼',41);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('30号楼',41);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('31号楼',41);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('32号楼',41);

--浙江传媒学院
insert into ssb_area(AREA_ID, area_name,area_region)values(51, '传媒生活区',6);

insert into ssb_dormitory(dormitory_name,dormitory_area)values('1号楼',51);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('2号楼',51);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('3号楼',51);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('4号楼',51);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('5号楼',51);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('6号楼',51);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('7号楼',51);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('8号楼',51);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('9号楼',51);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('10号楼',51);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('11号楼',51);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('12号楼',51);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('13号楼',51);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('14号楼',51);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('15号楼',51);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('16号楼',51);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('17号楼',51);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('18号楼',51);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('19号楼',51);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('20号楼',51);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('21号楼',51);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('研究生(留学生)楼',51);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('sa楼',51);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('sb楼',51);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('sc楼',51);
insert into ssb_dormitory(dormitory_name,dormitory_area)values('sd楼',51);
