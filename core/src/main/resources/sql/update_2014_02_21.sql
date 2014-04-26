//invoicing
insert into blc_admin_permission values(83,'invoicing','permission_all','READ');
insert into blc_admin_permission values(82,'invoicing','permission_all','CREATE');


INSERT INTO BLC_ADMIN_PERMISSION_ENTITY (ADMIN_PERMISSION_ENTITY_ID, CEILING_ENTITY, ADMIN_PERMISSION_ID) VALUES (247, 'com.ssbusy.core.invoicing.domain.Invoicing', 82);
INSERT INTO BLC_ADMIN_PERMISSION_ENTITY (ADMIN_PERMISSION_ENTITY_ID, CEILING_ENTITY, ADMIN_PERMISSION_ID) VALUES (248, 'com.ssbusy.core.invoicing.domain.Invoicing', 83);


insert into BLC_ADMIN_ROLE_PERMISSION_XREF (ADMIN_ROLE_ID, ADMIN_PERMISSION_ID) values(-1, 82);
insert into BLC_ADMIN_ROLE_PERMISSION_XREF (ADMIN_ROLE_ID, ADMIN_PERMISSION_ID) values(-1, 83);

INSERT INTO BLC_ADMIN_SECTION (ADMIN_SECTION_ID, ADMIN_MODULE_ID, NAME, SECTION_KEY, URL, USE_DEFAULT_HANDLER) VALUES (21, -1, '进销存', '进销存', '/Invoicing', false);
update  blc_admin_section set CEILING_ENTITY='com.ssbusy.core.invoicing.domain.Invoicing' where admin_section_id=21;
INSERT INTO blc_admin_sec_perm_xref (ADMIN_SECTION_ID, ADMIN_PERMISSION_ID) VALUES (21,82);
INSERT INTO blc_admin_sec_perm_xref (ADMIN_SECTION_ID, ADMIN_PERMISSION_ID) VALUES (21,83);

// 统计信息
INSERT INTO BLC_ADMIN_SECTION (ADMIN_SECTION_ID, ADMIN_MODULE_ID, NAME, SECTION_KEY, URL, USE_DEFAULT_HANDLER) VALUES (22, -2, '统计信息', '统计信息','/statistics', false);
update  blc_admin_section set CEILING_ENTITY='com.ssbusy.core.domain.AreaAddress' where admin_section_id=22;
INSERT INTO blc_admin_sec_perm_xref (ADMIN_SECTION_ID, ADMIN_PERMISSION_ID) VALUES (22,79);
INSERT INTO blc_admin_sec_perm_xref (ADMIN_SECTION_ID, ADMIN_PERMISSION_ID) VALUES (22,80);
INSERT INTO blc_admin_sec_perm_xref (ADMIN_SECTION_ID, ADMIN_PERMISSION_ID) VALUES (22,81);