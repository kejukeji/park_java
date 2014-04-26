
INSERT INTO BLC_ADMIN_PERMISSION (ADMIN_PERMISSION_ID, DESCRIPTION, NAME, PERMISSION_TYPE) VALUES (79,'Update Order','PERMISSION_UPDATE_ORDER_PROCESS', 'UPDATE');
INSERT INTO BLC_ADMIN_PERMISSION (ADMIN_PERMISSION_ID, DESCRIPTION, NAME, PERMISSION_TYPE) VALUES (80,'Read Order','PERMISSION_READ_ORDER_PROCESS', 'READ');
INSERT INTO BLC_ADMIN_PERMISSION (ADMIN_PERMISSION_ID, DESCRIPTION, NAME, PERMISSION_TYPE) VALUES (81,'All Order','PERMISSION_ALL_ORDER_PROCESS','ALL');

INSERT INTO BLC_ADMIN_PERMISSION_ENTITY (ADMIN_PERMISSION_ENTITY_ID, CEILING_ENTITY, ADMIN_PERMISSION_ID) VALUES (244, 'com.ssbusy.core.account.domain.MyCustomer', 79);
INSERT INTO BLC_ADMIN_PERMISSION_ENTITY (ADMIN_PERMISSION_ENTITY_ID, CEILING_ENTITY, ADMIN_PERMISSION_ID) VALUES (245, 'com.ssbusy.core.account.domain.MyCustomer', 80);
INSERT INTO BLC_ADMIN_PERMISSION_ENTITY (ADMIN_PERMISSION_ENTITY_ID, CEILING_ENTITY, ADMIN_PERMISSION_ID) VALUES (246, 'com.ssbusy.core.account.domain.MyCustomer', 81);

insert into BLC_ADMIN_ROLE_PERMISSION_XREF (ADMIN_ROLE_ID, ADMIN_PERMISSION_ID) values(-1, 81);
insert into BLC_ADMIN_ROLE_PERMISSION_XREF (ADMIN_ROLE_ID, ADMIN_PERMISSION_ID) values(-4, 81);

-- tell the admin it needs to load this section also and associate permissions
INSERT INTO BLC_ADMIN_SECTION (ADMIN_SECTION_ID, ADMIN_MODULE_ID, NAME, SECTION_KEY, URL, USE_DEFAULT_HANDLER) VALUES (-14, -1, 'SsbInventory', 'SsbInventory', '/ssbinventory', false);
update  blc_admin_section set CEILING_ENTITY='com.ssbusy.core.account.domain.MyCustomer' where admin_section_id=16;

INSERT INTO blc_admin_sec_perm_xref (ADMIN_SECTION_ID, ADMIN_PERMISSION_ID) VALUES (16,79);
INSERT INTO blc_admin_sec_perm_xref (ADMIN_SECTION_ID, ADMIN_PERMISSION_ID) VALUES (16,80);
INSERT INTO blc_admin_sec_perm_xref (ADMIN_SECTION_ID, ADMIN_PERMISSION_ID) VALUES (16,81);
