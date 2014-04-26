--
-- The Sample Project is configured with "hibernate.hbm2ddl.auto" value="create-drop" in "persistence.xml".
--
-- This will cause hibernate to populate the database when the application is started by processing the files that
-- were configured in the hibernate.hbm2ddl.import_files property.
--
-- This file is responsible for creating the content structure which includes page-types (e.g. template
-- mappings) and structured-content-types (e.g. ad placement among other things).
--

--
-- Load Currencies:  Your site will need currencies defined in order to use price lists.
-- Currencies need to be defined before locale if they are using a currency code.
-- i18n standard abbreviations should be used.
--
INSERT INTO BLC_CURRENCY(CURRENCY_CODE, FRIENDLY_NAME, DEFAULT_FLAG) VALUES('CNY', '元', TRUE);
INSERT INTO BLC_CURRENCY(CURRENCY_CODE, FRIENDLY_NAME, DEFAULT_FLAG) VALUES('AUD', '丹', FALSE);

--
-- Load Locales:  Your site must have at least one Locale with DEFAULT_FLAG set to TRUE
-- You can have as many locales as you like. Currency can be set  to null if none have
-- been defined. i18n standard abbreviations should be used.
--
-- INSERT INTO BLC_LOCALE (LOCALE_CODE, DEFAULT_FLAG, FRIENDLY_NAME, CURRENCY_CODE) VALUES ('en_US', TRUE, 'English US', 'USD');
-- INSERT INTO BLC_LOCALE (LOCALE_CODE, DEFAULT_FLAG, FRIENDLY_NAME, CURRENCY_CODE) VALUES ('en', FALSE, 'English', 'USD');
INSERT INTO BLC_LOCALE (LOCALE_CODE, DEFAULT_FLAG, FRIENDLY_NAME, CURRENCY_CODE) VALUES ('zh_CN', TRUE, '简体中文', 'CNY');

--
-- The following items create page templates.   The key is to map a JSP template (TMPLT_PATH) to
-- a LOCALE_CODE.   In the example below, there is only one JSP template "basic".   The full
-- path to the template is .../WEB_INF/jsp/templates/basic.jsp.
--
INSERT INTO BLC_PAGE_TMPLT (PAGE_TMPLT_ID, LOCALE_CODE, TMPLT_NAME, TMPLT_DESCR, TMPLT_PATH) VALUES (1, 'zh_CN', '基本模板', '本模板提供header, footer布局的内容和标题', '/content/default') ;

--
-- Field groups define a list of dynamic fields.    Field groups can be associated with page
-- templates or structured content types.    The field group below defines two fields named
-- "body", which is a rich text edit field, and "title", which is a string field.
-- This will tell the Broadleaf admin how to generate the view for a given template
--
INSERT INTO BLC_FLD_GROUP(FLD_GROUP_ID, NAME, INIT_COLLAPSED_FLAG) VALUES (1, 'Content', FALSE);
INSERT INTO BLC_FLD_DEF(FLD_DEF_ID, NAME, FRIENDLY_NAME, FLD_TYPE, SECURITY_LEVEL, HIDDEN_FLAG, VLDTN_REGEX, VLDTN_ERROR_MSSG_KEY, MAX_LENGTH, COLUMN_WIDTH, TEXT_AREA_FLAG, FLD_ENUM_ID, ALLOW_MULTIPLES, FLD_GROUP_ID, FLD_ORDER) VALUES (3, 'title', 'Title', 'STRING', NULL, FALSE, null, null, null, '*', FALSE, NULL, FALSE, 1, 0);
INSERT INTO BLC_FLD_DEF(FLD_DEF_ID, NAME, FRIENDLY_NAME, FLD_TYPE, SECURITY_LEVEL, HIDDEN_FLAG, VLDTN_REGEX, VLDTN_ERROR_MSSG_KEY, MAX_LENGTH, COLUMN_WIDTH, TEXT_AREA_FLAG, FLD_ENUM_ID, ALLOW_MULTIPLES, FLD_GROUP_ID, FLD_ORDER) VALUES (2, 'body', 'Body', 'HTML', NULL, FALSE, null, null, null, '*', FALSE, NULL, FALSE, 1, 1);
insert into blc_sc_type (SC_TYPE_ID, DESCRIPTION, NAME, SC_FLD_TMPLT_ID) values (5, null, '公告牌', 4);
insert into blc_sc_type (SC_TYPE_ID, DESCRIPTION, NAME, SC_FLD_TMPLT_ID) values (6, null, '爆款牌', 4);

--
-- Map both the english template to this field group.
--
INSERT INTO BLC_PGTMPLT_FLDGRP_XREF(PAGE_TMPLT_ID, FLD_GROUP_ID, GROUP_ORDER) VALUES (1,1,0);


-----------------------------------------------------------------------------------------------------------------------------------
-- Structured Content Step 1:   Create Structured Content Field Groups
-----------------------------------------------------------------------------------------------------------------------------------
-- Create Ad Fields - Defining the ad field group as a group of two fields: Image URL and Target URL
INSERT INTO BLC_FLD_GROUP(FLD_GROUP_ID, NAME, INIT_COLLAPSED_FLAG) VALUES (4, '图链字段', FALSE);
INSERT INTO BLC_FLD_DEF(FLD_DEF_ID, NAME, FRIENDLY_NAME, FLD_TYPE, SECURITY_LEVEL, HIDDEN_FLAG, VLDTN_REGEX, VLDTN_ERROR_MSSG_KEY, MAX_LENGTH, COLUMN_WIDTH, TEXT_AREA_FLAG, FLD_ENUM_ID, ALLOW_MULTIPLES, FLD_GROUP_ID, FLD_ORDER) VALUES (7, 'imageUrl', 'Image URL', 'STRING', NULL, FALSE, null, null, 150, '*', FALSE, NULL, FALSE, 4, 0);
INSERT INTO BLC_FLD_DEF(FLD_DEF_ID, NAME, FRIENDLY_NAME, FLD_TYPE, SECURITY_LEVEL, HIDDEN_FLAG, VLDTN_REGEX, VLDTN_ERROR_MSSG_KEY, MAX_LENGTH, COLUMN_WIDTH, TEXT_AREA_FLAG, FLD_ENUM_ID, ALLOW_MULTIPLES, FLD_GROUP_ID, FLD_ORDER) VALUES (8, 'targetUrl', 'Target URL', 'STRING', NULL, FALSE, null, null, 150, '*', FALSE, NULL, FALSE, 4, 1);

-- Create HTML Fields - Defining the message field group as a single field called messageText
INSERT INTO BLC_FLD_GROUP(FLD_GROUP_ID, NAME, INIT_COLLAPSED_FLAG) VALUES (5, 'HTML字段', FALSE);
INSERT INTO BLC_FLD_DEF(FLD_DEF_ID, NAME, FRIENDLY_NAME, FLD_TYPE, SECURITY_LEVEL, HIDDEN_FLAG, VLDTN_REGEX, VLDTN_ERROR_MSSG_KEY, MAX_LENGTH, COLUMN_WIDTH, TEXT_AREA_FLAG, FLD_ENUM_ID, ALLOW_MULTIPLES, FLD_GROUP_ID, FLD_ORDER) VALUES (10, 'htmlContent', 'HTML Content', 'HTML', NULL, FALSE, null, null, null, '*', FALSE, NULL, FALSE, 5, 0);

-- Create Message Fields - Defining the message field group as a single field called messageText
INSERT INTO BLC_FLD_GROUP(FLD_GROUP_ID, NAME, INIT_COLLAPSED_FLAG) VALUES (6, '文本字段', FALSE);
INSERT INTO BLC_FLD_DEF(FLD_DEF_ID, NAME, FRIENDLY_NAME, FLD_TYPE, SECURITY_LEVEL, HIDDEN_FLAG, VLDTN_REGEX, VLDTN_ERROR_MSSG_KEY, MAX_LENGTH, COLUMN_WIDTH, TEXT_AREA_FLAG, FLD_ENUM_ID, ALLOW_MULTIPLES, FLD_GROUP_ID, FLD_ORDER) VALUES (9, 'messageText', 'Message Text', 'STRING', NULL, FALSE, null, null, 150, '*', FALSE, NULL, FALSE, 6, 0);

INSERT INTO BLC_FLD_GROUP(FLD_GROUP_ID, NAME, INIT_COLLAPSED_FLAG) VALUES (7, 'HTML链字段', FALSE);
insert into blc_fld_def (FLD_DEF_ID, ALLOW_MULTIPLES, COLUMN_WIDTH, FLD_ORDER, FLD_TYPE, FRIENDLY_NAME,HIDDEN_FLAG, MAX_LENGTH, NAME, SECURITY_LEVEL, TEXT_AREA_FLAG, VLDTN_ERROR_MSSG_KEY, VLDTN_REGEX, FLD_ENUM_ID, FLD_GROUP_ID)values (11, false, '*', 0, 'STRING', 'Title', false, 150, 'title', null, false, null, null, null, 7);
insert into blc_fld_def (FLD_DEF_ID, ALLOW_MULTIPLES, COLUMN_WIDTH, FLD_ORDER, FLD_TYPE, FRIENDLY_NAME,HIDDEN_FLAG, MAX_LENGTH, NAME, SECURITY_LEVEL, TEXT_AREA_FLAG, VLDTN_ERROR_MSSG_KEY, VLDTN_REGEX, FLD_ENUM_ID, FLD_GROUP_ID)values (12, false, '*', 1, 'HTML', 'HTML Content', false, 150, 'htmlContent', null, false, null, null, null, 7);
insert into blc_fld_def (FLD_DEF_ID, ALLOW_MULTIPLES, COLUMN_WIDTH, FLD_ORDER, FLD_TYPE, FRIENDLY_NAME,HIDDEN_FLAG, MAX_LENGTH, NAME, SECURITY_LEVEL, TEXT_AREA_FLAG, VLDTN_ERROR_MSSG_KEY, VLDTN_REGEX, FLD_ENUM_ID, FLD_GROUP_ID)values (13, false, '*', 2, 'STRING', 'Target URL', false, 150, 'targetUrl', null, false, null, null, null, 7);

-----------------------------------------------------------------------------------------------------------------------------------
-- Structured Content Step 2:   Create Templates.     The examples below create field templates for Ads, Messages, and HTML blocks.
-----------------------------------------------------------------------------------------------------------------------------------
INSERT INTO BLC_SC_FLD_TMPLT(SC_FLD_TMPLT_ID, NAME) VALUES(1, '图片链接模板');
INSERT INTO BLC_SC_FLD_TMPLT(SC_FLD_TMPLT_ID, NAME) VALUES(2, 'HTML模板');
INSERT INTO BLC_SC_FLD_TMPLT(SC_FLD_TMPLT_ID, NAME) VALUES(3, '文本模板');
insert into blc_sc_fld_tmplt (SC_FLD_TMPLT_ID, name)values(4, 'HTML链接模板');

-----------------------------------------------------------------------------------------------------------------------------------
-- Structured Content Step 3:   Add Field Groups to Templates
-----------------------------------------------------------------------------------------------------------------------------------
INSERT INTO BLC_SC_FLDGRP_XREF(SC_FLD_TMPLT_ID, FLD_GROUP_ID, GROUP_ORDER) VALUES (1,4,0);
INSERT INTO BLC_SC_FLDGRP_XREF(SC_FLD_TMPLT_ID, FLD_GROUP_ID, GROUP_ORDER) VALUES (2,5,0);
INSERT INTO BLC_SC_FLDGRP_XREF(SC_FLD_TMPLT_ID, FLD_GROUP_ID, GROUP_ORDER) VALUES (3,6,0);
insert into blc_sc_fldgrp_xref (SC_FLD_TMPLT_ID, FLD_GROUP_ID, GROUP_ORDER)values(4, 7, 0);

-----------------------------------------------------------------------------------------------------------------------------------
-- Structured Content Step 4:   Create Types (These represent areas on a page or general types:  e.g 'Homepage Banner Ad')
-----------------------------------------------------------------------------------------------------------------------------------
INSERT INTO BLC_SC_TYPE (SC_TYPE_ID, NAME, DESCRIPTION, SC_FLD_TMPLT_ID) VALUES (1, '首页幻灯', NULL, 1);
INSERT INTO BLC_SC_TYPE (SC_TYPE_ID, NAME, DESCRIPTION, SC_FLD_TMPLT_ID) VALUES (2, 'Homepage Middle Promo Snippet', NULL, 2);
INSERT INTO BLC_SC_TYPE (SC_TYPE_ID, NAME, DESCRIPTION, SC_FLD_TMPLT_ID) VALUES (3, 'Homepage Featured Products Title', NULL, 3);
INSERT INTO BLC_SC_TYPE (SC_TYPE_ID, NAME, DESCRIPTION, SC_FLD_TMPLT_ID) VALUES (4, '首页品牌', NULL, 1);

